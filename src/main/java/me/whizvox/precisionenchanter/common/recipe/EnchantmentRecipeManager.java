package me.whizvox.precisionenchanter.common.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentRecipeManager extends SimpleJsonResourceReloadListener {

  private static final Gson GSON = new GsonBuilder().registerTypeAdapter(EnchantmentRecipe.class, new EnchantmentRecipe.Serializer()).create();

  private boolean initialized;
  private final Map<ResourceLocation, EnchantmentRecipe> recipes;
  private final List<EnchantmentRecipe> byNumId;
  private final Map<ResourceLocation, Integer> reverseByNumId;
  private final Map<Enchantment, Map<Integer, EnchantmentRecipe>> byEnchantment;

  public EnchantmentRecipeManager() {
    super(GSON, "enchantment_recipes");
    initialized = false;
    recipes = new HashMap<>();
    byNumId = new ArrayList<>();
    reverseByNumId = new HashMap<>();
    byEnchantment = new HashMap<>();
  }

  private void add(EnchantmentRecipe recipe) {
    recipes.put(recipe.getId(), recipe);
    byEnchantment.computeIfAbsent(recipe.getEnchantment(), enchantment -> new Int2ObjectArrayMap<>()).put(recipe.getLevel(), recipe);
    int numId = byNumId.size();
    byNumId.add(recipe);
    reverseByNumId.put(recipe.getId(), numId);
  }

  private void markInitialized() {
    initialized = true;
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
    return new SyncEnchantmentRecipesMessage(List.copyOf(recipes.values()));
  }

  @Nullable
  public EnchantmentRecipe get(ResourceLocation id) {
    if (isInitialized()) {
      return recipes.get(id);
    }
    return null;
  }

  @Nullable
  public EnchantmentRecipe get(int id) {
    if (isInitialized()) {
      return byNumId.get(id);
    }
    return null;
  }

  @Nullable
  public Integer getNumericalId(ResourceLocation id) {
    if (isInitialized()) {
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

  /**
   * Clears all recipes from this manager. Should only be called when a server reload has happened.
   */
  public void clear() {
    synchronized (this) {
      recipes.clear();
      byNumId.clear();
      reverseByNumId.clear();
      byEnchantment.clear();
      initialized = false;
    }
  }

  // only done by the server
  @Override
  protected void apply(Map<ResourceLocation, JsonElement> entries, ResourceManager manager, ProfilerFiller profiler) {
    clear();
    synchronized (this) {
      entries.forEach((location, json) -> add(deserialize(json)));
      PELog.LOGGER.info(PELog.M_SERVER, "{} enchantment recipes loaded", recipes.size());
      ForgeRegistries.ENCHANTMENTS.forEach(enchantment -> {
        var byLevel = byEnchantment.get(enchantment);
        if (byLevel == null) {
          PELog.LOGGER.debug(PELog.M_SERVER, "Enchantment {} has no recipes", ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
        } else {
          int maxLevel = enchantment.getMaxLevel();
          for (int i = 0; i < maxLevel; i++) {
            if (!byLevel.containsKey(i)) {
              PELog.LOGGER.debug(PELog.M_SERVER, "Enchantment {} is missing a recipe at level {}", ForgeRegistries.ENCHANTMENTS.getKey(enchantment), i + 1);
            }
          }
        }
      });
      markInitialized();
    }
    // apparently a reload happens upon a server shutting down, so we first have to check if the server is still online
    if (ServerLifecycleHooks.getCurrentServer() != null) {
      PENetwork.broadcast(SimpleClientBoundMessage.ENCHANTMENT_RECIPES_RELOADED);
    }
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

  public List<Pair<EnchantmentRecipe, EnchantmentRecipe.MatchResult>> match(ItemStack stackToEnchant, Container container) {
    // want to allow the application of enchantments onto already-enchanted item stacks, which vanilla disallows in the
    // ItemStack#isEnchantable() method. The Item#isEnchantable(ItemStack) method does not check if the item is already
    // enchanted, so use that instead.
    if (stackToEnchant.isEmpty() || !stackToEnchant.getItem().isEnchantable(stackToEnchant)) {
      return List.of();
    }
    List<Pair<EnchantmentRecipe, EnchantmentRecipe.MatchResult>> matchedRecipes = new ArrayList<>();
    if (stackToEnchant.is(Items.BOOK)) {
      recipes.values().stream()
          .filter(recipe -> !recipe.isInvalid())
          .forEach(recipe -> {
            EnchantmentRecipe.MatchResult result = recipe.match(stackToEnchant, container);
            if (result.matches()) {
              matchedRecipes.add(Pair.of(recipe, result));
            }
          });
    } else {
      byEnchantment.entrySet().stream()
          .filter(entry -> entry.getKey().canEnchant(stackToEnchant))
          .forEach(entry -> {
            // do this instead of ItemStack#getEnchantmentLevel(...) to account for enchanted books as well
            int currentLevel = EnchantmentHelper.getEnchantments(stackToEnchant).getOrDefault(entry.getKey(), 0);
            entry.getValue().entrySet().stream()
                .filter(entry2 -> !entry2.getValue().isInvalid() && entry2.getKey() >= currentLevel)
                .map(Map.Entry::getValue)
                .forEach(recipe -> {
                  EnchantmentRecipe.MatchResult result = recipe.match(stackToEnchant, container);
                  if (result.matches()) {
                    matchedRecipes.add(Pair.of(recipe, result));
                  }
                });
          });
    }
    return matchedRecipes;
  }

  public static ItemStack createResultStack(ItemStack stack, Enchantment enchantment, int level) {
    ItemStack result;
    if (stack.is(Items.BOOK)) {
      result = new ItemStack(Items.BOOK);
      EnchantmentHelper.setEnchantments(Map.of(enchantment, level), result);
    } else {
      result = stack.copy();
      var enchantments = result.getAllEnchantments();
      enchantments.put(enchantment, level);
      EnchantmentHelper.setEnchantments(enchantments, result);
    }
    return result;
  }

  public static final EnchantmentRecipeManager INSTANCE = new EnchantmentRecipeManager();

  public static JsonElement serialize(EnchantmentRecipe recipe) {
    return GSON.toJsonTree(recipe);
  }

  public static EnchantmentRecipe deserialize(JsonElement json) {
    return GSON.fromJson(json, EnchantmentRecipe.class);
  }

}
