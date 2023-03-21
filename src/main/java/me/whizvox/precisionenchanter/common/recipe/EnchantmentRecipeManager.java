package me.whizvox.precisionenchanter.common.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import me.whizvox.precisionenchanter.common.api.EnchantmentStorageManager;
import me.whizvox.precisionenchanter.common.api.IEnchantmentStorage;
import me.whizvox.precisionenchanter.common.api.condition.ConditionFailedException;
import me.whizvox.precisionenchanter.common.api.NoSuchEnchantmentException;
import me.whizvox.precisionenchanter.common.api.condition.LoadStage;
import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.SimpleClientBoundMessage;
import me.whizvox.precisionenchanter.common.network.message.SyncEnchantmentRecipesMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EnchantmentRecipeManager extends SimpleJsonResourceReloadListener {

  private static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(EnchantmentRecipe.class, EnchantmentRecipe.SERIALIZER)
      .registerTypeAdapter(EnchantmentRecipe.Immutable.class, EnchantmentRecipe.SERIALIZER)
      .registerTypeAdapter(ConditionalEnchantmentRecipe.class, ConditionalEnchantmentRecipe.SERIALIZER)
      .registerTypeAdapter(ConditionalEnchantmentRecipe.Immutable.class, ConditionalEnchantmentRecipe.SERIALIZER)
      .create();

  private boolean initialized;
  private boolean deferredRecipesChecked;
  private final Map<ResourceLocation, EnchantmentRecipe> recipes;
  private final List<EnchantmentRecipe> byNumId;
  private final Map<ResourceLocation, Integer> reverseByNumId;
  private final Map<Enchantment, Map<Integer, EnchantmentRecipe>> byEnchantment;
  private final Map<ResourceLocation, ConditionalEnchantmentRecipe> deferredRecipes;

  public EnchantmentRecipeManager() {
    super(GSON, "enchantment_recipes");
    initialized = false;
    deferredRecipesChecked = false;
    recipes = new HashMap<>();
    byNumId = new ArrayList<>();
    reverseByNumId = new HashMap<>();
    byEnchantment = new HashMap<>();
    deferredRecipes = new HashMap<>();
  }

  private void add(EnchantmentRecipe recipe) {
    recipe = recipe.immutable();
    if (recipe.isInvalid()) {
      PELog.LOGGER.warn(PELog.side(), "Attempted to register invalid recipe (no ingredients, unset enchantment, or unset ID): {}", recipe.getId());
    } else {
      recipes.put(recipe.getId(), recipe);
      byEnchantment.computeIfAbsent(recipe.getEnchantment(), enchantment -> new Int2ObjectArrayMap<>()).put(recipe.getLevel(), recipe);
      int numId = byNumId.size();
      byNumId.add(recipe);
      reverseByNumId.put(recipe.getId(), numId);
      if (recipe instanceof ConditionalEnchantmentRecipe condRecipe && condRecipe.condition.shouldDefer()) {
        deferredRecipes.put(recipe.getId(), condRecipe);
      }
    }
  }

  private void markInitialized() {
    initialized = true;
    if (deferredRecipes.isEmpty()) {
      deferredRecipesChecked = true;
    }
  }

  private void checkDeferredRecipes() {
    if (initialized && !deferredRecipesChecked) {
      synchronized (this) {
        int initialSize = recipes.size();
        PELog.LOGGER.debug("Beginning deferred test of {} recipes", deferredRecipes.size());
        deferredRecipes.forEach((id, recipe) -> {
          if (!recipe.condition.test(LoadStage.POST_LOAD)) {
            recipes.remove(id);
            PELog.LOGGER.debug("Removed recipe {} due to failed condition", id);
          }
        });
        if (recipes.size() == initialSize) {
          PELog.LOGGER.debug("Removed 0 recipes");
        } else {
          PELog.LOGGER.info("Removed {} recipes", initialSize - recipes.size());
        }
        deferredRecipesChecked = true;
      }
    }
  }

  /**
   * @return True if recipes have been loaded from a reload or a sync packet, false if this hasn't yet happened of if
   * {@link #clear()} has been called.
   */
  public boolean isInitialized() {
    synchronized (this) {
      return initialized;
    }
  }

  public SyncEnchantmentRecipesMessage createSyncMessage() {
    checkDeferredRecipes();
    return new SyncEnchantmentRecipesMessage(List.copyOf(recipes.values()));
  }

  @Nullable
  public EnchantmentRecipe get(ResourceLocation id) {
    if (isInitialized()) {
      checkDeferredRecipes();
      return recipes.get(id);
    }
    return null;
  }

  @Nullable
  public EnchantmentRecipe get(int id) {
    if (isInitialized()) {
      checkDeferredRecipes();
      return byNumId.get(id);
    }
    return null;
  }

  @Nullable
  public Integer getNumericalId(ResourceLocation id) {
    if (isInitialized()) {
      checkDeferredRecipes();
      return reverseByNumId.get(id);
    }
    return null;
  }

  @Nullable
  public Integer getNumericalId(EnchantmentRecipe recipe) {
    if (isInitialized()) {
      return getNumericalId(recipe.getId());
    }
    return null;
  }

  @Nullable
  public EnchantmentRecipe get(Enchantment enchantment, int level) {
    checkDeferredRecipes();
    return byEnchantment.getOrDefault(enchantment, Map.of()).getOrDefault(level, null);
  }

  public Stream<Map.Entry<ResourceLocation, EnchantmentRecipe>> entryStream() {
    checkDeferredRecipes();
    return recipes.entrySet().stream();
  }

  public Stream<EnchantmentRecipe> stream() {
    checkDeferredRecipes();
    return recipes.values().stream();
  }

  /**
   * Clears all recipes from this manager. Should only be called when a server reload has happened.
   */
  public void clear() {
    synchronized (this) {
      recipes.clear();
      byNumId.clear();
      reverseByNumId.clear();
      byEnchantment.clear();
      deferredRecipes.clear();
      initialized = false;
      deferredRecipesChecked = false;
    }
  }

  // only done by the server
  @Override
  protected void apply(Map<ResourceLocation, JsonElement> entries, ResourceManager manager, ProfilerFiller profiler) {
    clear();
    synchronized (this) {
      entries.forEach((location, json) -> {
        try {
          ConditionalEnchantmentRecipe recipe = deserialize(json);
          recipe.setId(location);
          add(recipe.immutable());
        } catch (ConditionFailedException e) {
          PELog.LOGGER.debug("Skipping enchantment recipe {} due to failed condition", location);
        } catch (NoSuchEnchantmentException e) {
          PELog.LOGGER.warn("Unknown enchantment while parsing recipe: {}", e.id);
        }
      });
      PELog.LOGGER.info(PELog.M_SERVER, "{} enchantment recipes loaded ({} deferred)", recipes.size(), deferredRecipes.size());
      ForgeRegistries.ENCHANTMENTS.forEach(enchantment -> {
        var byLevel = byEnchantment.get(enchantment);
        if (byLevel == null) {
          PELog.LOGGER.debug(PELog.M_SERVER, "Enchantment {} has no recipes", ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
        } else {
          int maxLevel = enchantment.getMaxLevel();
          for (int i = 1; i <= maxLevel; i++) {
            if (!byLevel.containsKey(i)) {
              PELog.LOGGER.debug(PELog.M_SERVER, "Enchantment {} is missing a recipe at level {}", ForgeRegistries.ENCHANTMENTS.getKey(enchantment), i);
            }
          }
        }
      });
      markInitialized();
    }
    // apparently a reload happens upon a server shutting down, so we first have to check if the server is still online
    if (ServerLifecycleHooks.getCurrentServer() != null && FMLEnvironment.dist.isDedicatedServer()) {
      PENetwork.broadcast(SimpleClientBoundMessage.ENCHANTMENT_RECIPES_RELOADED);
    }
  }

  /**
   * Find all enchantment recipes that cannot be crafted due to having at least one ingredient that has no items. Most
   * likely, this will happen with empty tags. However, even if a tag isn't empty, it will resolve to such if this
   * method is called too early, and will mess up all recipes that use this tag. I have no idea why this can even
   * happen, but there you go.
   * @return All enchantment recipes that are impossible to craft
   */
  public List<EnchantmentRecipe> findImpossibleRecipes() {
    return stream().filter(recipe -> recipe.getIngredients().stream().anyMatch(pair -> {
      boolean impossible = true;
      for (ItemStack item : pair.getLeft().getItems()) {
        if (item.getItem().getMaxStackSize(item) >= pair.getRight()) {
          impossible = false;
          break;
        }
      }
      return impossible;
    })).toList();
  }

  public List<EnchantmentRecipe> findFreeRecipes() {
    return stream()
        .filter(recipe -> recipe.getCost() <= 0)
        .toList();
  }

  /**
   * Sync with an external list of enchantment recipes. Should only be called on the client when the player needs to
   * view enchantment recipes. Can also call preemptively (i.e. on login) if wanted.
   * @param newRecipes The recipes to add to this manager
   */
  @OnlyIn(Dist.CLIENT)
  public void sync(List<EnchantmentRecipe> newRecipes) {
    clear();
    synchronized (this) {
      newRecipes.forEach(this::add);
      markInitialized();
    }
  }

  public List<EnchantmentRecipe> match(Container container) {
    return stream().filter(recipe -> !recipe.isInvalid() && recipe.match(container)).toList();
  }

  public List<Pair<EnchantmentRecipe, EnchantmentRecipe.MatchResult>> match(ItemStack stackToEnchant, Container container) {
    IEnchantmentStorage storage = EnchantmentStorageManager.INSTANCE.findMatch(stackToEnchant);
    if (storage == null) {
      return List.of();
    }
    List<Pair<EnchantmentRecipe, EnchantmentRecipe.MatchResult>> matchedRecipes = new ArrayList<>();
    stream()
        .filter(recipe -> !recipe.isInvalid())
        .forEach(recipe -> {
          EnchantmentInstance instance = new EnchantmentInstance(recipe.getEnchantment(), recipe.getLevel());
          if (storage.canApply(stackToEnchant, instance)) {
            EnchantmentRecipe.MatchResult result = recipe.match(stackToEnchant, container, storage);
            if (result.matches()) {
              matchedRecipes.add(Pair.of(recipe, result));
            }
          }
        });
    return matchedRecipes;
  }

  public static final EnchantmentRecipeManager INSTANCE = new EnchantmentRecipeManager();

  public static JsonElement serialize(EnchantmentRecipe recipe) {
    return GSON.toJsonTree(recipe);
  }

  public static ConditionalEnchantmentRecipe deserialize(JsonElement json) {
    return GSON.fromJson(json, ConditionalEnchantmentRecipe.class);
  }

}
