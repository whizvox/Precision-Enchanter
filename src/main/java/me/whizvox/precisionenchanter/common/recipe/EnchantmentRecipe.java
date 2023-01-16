package me.whizvox.precisionenchanter.common.recipe;

import com.google.gson.*;
import me.whizvox.precisionenchanter.common.api.EnchantmentStorageManager;
import me.whizvox.precisionenchanter.common.api.IEnchantmentStorage;
import me.whizvox.precisionenchanter.common.lib.PELog;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EnchantmentRecipe {

  private final ResourceLocation id;
  private final List<Pair<Ingredient, Integer>> ingredients;
  private final Enchantment enchantment;
  private final int level;

  EnchantmentRecipe(ResourceLocation id, List<Pair<Ingredient, Integer>> ingredients, Enchantment enchantment, int level) {
    this.id = id;
    this.ingredients = ingredients.stream().filter(pair -> !pair.getLeft().isEmpty()).toList();
    this.enchantment = enchantment;
    this.level = level;

    if (isInvalid() && id != null) {
      List<String> problems = new ArrayList<>(2);
      if (ingredients.isEmpty()) {
        problems.add("no ingredients");
      }
      if (enchantment == null) {
        problems.add("result not defined");
      }
      PELog.LOGGER.warn(PELog.M_SERVER, "Invalid precision enchantment recipe ({}): {}", id, String.join(", ", problems));
    }
  }

  public boolean isInvalid() {
    return ingredients.isEmpty() || enchantment == null;
  }

  public ResourceLocation getId() {
    return id;
  }

  public List<Pair<Ingredient, Integer>> getIngredients() {
    return ingredients;
  }

  public Enchantment getEnchantment() {
    return enchantment;
  }

  public int getLevel() {
    return level;
  }

  /**
   * Attempt to match contents of recipe to contents of container.
   * @param container The container with ingredients (ItemStacks)
   * @return A map of int pairs (key = slot, value = count to shrink by), or an empty map if no match was found
   */
  public MatchResult match(ItemStack stackToEnchant, Container container, IEnchantmentStorage storage) {
    if (isInvalid()) {
      return MatchResult.FALSE;
    }
    ItemStackHandler invCopy = new ItemStackHandler(container.getContainerSize());
    for (int i = 0; i < container.getContainerSize(); i++) {
      invCopy.setStackInSlot(i, container.getItem(i).copy());
    }
    List<Pair<Ingredient, Integer>> remaining = new ArrayList<>(ingredients.size());
    ingredients.forEach(pair -> remaining.add(MutablePair.of(pair.getLeft(), pair.getRight())));
    for (int i = 0; i < invCopy.getSlots(); i++) {
      ItemStack stack = invCopy.getStackInSlot(i);
      for (int j = 0; j < remaining.size(); j++) {
        Pair<Ingredient, Integer> pair = remaining.get(j);
        if (pair.getLeft().test(stack)) {
          int count = pair.getRight();
          int diff = count - stack.getCount();
          stack.shrink(count);
          if (diff <= 0) {
            remaining.remove(pair);
          } else {
            pair.setValue(diff);
          }
        }
      }
    }
    if (remaining.isEmpty()) {
      List<ItemStack> resultingStacks = new ArrayList<>(invCopy.getSlots());
      for (int i = 0; i < invCopy.getSlots(); i++) {
        resultingStacks.add(invCopy.getStackInSlot(i));
      }
      EnchantmentInstance instance = new EnchantmentInstance(enchantment, level);
      ItemStack result = storage.applyEnchantment(stackToEnchant, instance);
      int rarity = switch (enchantment.getRarity()) {
        case COMMON -> 3;
        case UNCOMMON -> 5;
        case RARE -> 7;
        case VERY_RARE -> 9;
      };
      int cost = rarity * (instance.level - storage.getEnchantments(stackToEnchant).getOrDefault(enchantment, 0));
      return new MatchResult(true, resultingStacks, result, cost);
    }
    return MatchResult.FALSE;
  }

  public MatchResult match(ItemStack stackToEnchant, Container container) {
    IEnchantmentStorage storage = EnchantmentStorageManager.INSTANCE.findMatch(stackToEnchant);
    if (storage == null) {
      return MatchResult.FALSE;
    }
    return match(stackToEnchant, container, storage);
  }

  public void toNetwork(FriendlyByteBuf buf) {
    buf.writeResourceLocation(id);
    if (isInvalid()) {
      buf.writeBoolean(false);
    } else {
      buf.writeBoolean(true);
      buf.writeByte(ingredients.size());
      ingredients.forEach(pair -> {
        pair.getLeft().toNetwork(buf);
        buf.writeByte(pair.getRight());
      });
      buf.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
      buf.writeShort(level);
    }
  }

  public static EnchantmentRecipe fromNetwork(FriendlyByteBuf buf) {
    ResourceLocation id = buf.readResourceLocation();
    Builder builder = builder(id);
    if (buf.readBoolean()) {
      int numIngredients = buf.readByte();
      for (int j = 0; j < numIngredients; j++) {
        builder.ingredient(Ingredient.fromNetwork(buf), buf.readByte());
      }
      ResourceLocation enchantmentId = buf.readResourceLocation();
      Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(enchantmentId);
      if (enchantment == null) {
        throw new IllegalArgumentException("Unknown enchantment ID: " + enchantmentId);
      }
      builder.result(enchantment, buf.readShort());
    }
    return builder.build();
  }

  public static final EnchantmentRecipe INVALID = new EnchantmentRecipe(null, List.of(), null, 0);

  public record MatchResult(boolean matches, List<ItemStack> leftoverIngredients, ItemStack result, int cost) {

    public static final MatchResult FALSE = new MatchResult(false, List.of(), ItemStack.EMPTY, 0);

  }

  public static boolean canCraftEnchantment(Player player, int cost) {
    return player.getAbilities().instabuild || player.experienceLevel >= cost;
  }

  public static Builder builder(ResourceLocation id) {
    return new Builder(id);
  }

  public static class Builder {
    private final ResourceLocation id;
    private final List<Pair<Ingredient, Integer>> ingredients;
    private Enchantment result;
    private int level;
    public Builder(ResourceLocation id) {
      this.id = id;
      ingredients = new ArrayList<>();
      result = null;
      level = 1;
    }
    public Builder ingredient(Ingredient ingredient, int count) {
      ingredients.add(Pair.of(ingredient, count));
      return this;
    }
    public Builder ingredient(Ingredient ingredient) {
      return ingredient(ingredient, 1);
    }
    public Builder ingredient(ItemLike item, int count) {
      return ingredient(Ingredient.of(item), count);
    }
    public Builder ingredient(ItemLike item) {
      return ingredient(item, 1);
    }
    public Builder ingredient(TagKey<Item> tag, int count) {
      return ingredient(Ingredient.of(tag), count);
    }
    public Builder ingredient(TagKey<Item> tag) {
      return ingredient(tag, 1);
    }
    public Builder result(Enchantment result, int level) {
      this.result = result;
      this.level = level;
      return this;
    }
    public EnchantmentRecipe build() {
      return new EnchantmentRecipe(id, ingredients, result, Mth.clamp(level, 1, Short.MAX_VALUE));
    }
  }

  public static class Serializer implements JsonSerializer<EnchantmentRecipe>, JsonDeserializer<EnchantmentRecipe> {
    @Override
    public JsonElement serialize(EnchantmentRecipe src, Type type, JsonSerializationContext ctx) {
      JsonObject json = new JsonObject();
      json.addProperty("id", src.id.toString());
      JsonArray ingredientsArr = new JsonArray(src.ingredients.size());
      src.ingredients.forEach(pair -> {
        JsonObject ingredientJson = new JsonObject();
        ingredientJson.add("ingredient", pair.getLeft().toJson());
        ingredientJson.addProperty("count", pair.getRight());
        ingredientsArr.add(ingredientJson);
      });
      json.add("ingredients", ingredientsArr);
      JsonObject resultJson = new JsonObject();
      resultJson.addProperty("enchantment", ForgeRegistries.ENCHANTMENTS.getKey(src.enchantment).toString());
      resultJson.addProperty("level", src.level);
      json.add("result", resultJson);
      return json;
    }
    @Override
    public EnchantmentRecipe deserialize(JsonElement jsonRaw, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      JsonObject json = jsonRaw.getAsJsonObject();
      JsonElement idJson = json.get("id");
      if (idJson == null) {
        throw new JsonParseException("Precision enchantment recipe must include an ID field (`id`)");
      }
      String idStr = idJson.getAsString();
      ResourceLocation id = ResourceLocation.tryParse(idStr);
      if (id == null) {
        throw new JsonParseException("Invalid ID for precision enchantment recipe, must be a proper resource location: " + idStr);
      }
      if (id.getNamespace().equals("minecraft")) {
        PELog.LOGGER.warn(PELog.M_SERVER, "Default namespace is not recommended for precision enchantment recipe: {}", id);
      }
      Builder builder = builder(id);
      JsonElement ingredientsJson = json.get("ingredients");
      if (ingredientsJson != null) {
        ingredientsJson.getAsJsonArray().forEach(ingredientRawJson -> {
          JsonObject ingredientJson = ingredientRawJson.getAsJsonObject();
          builder.ingredient(Ingredient.fromJson(ingredientJson.get("ingredient")), ingredientJson.get("count").getAsInt());
        });
      }
      JsonElement resultRawJson = json.get("result");
      if (resultRawJson != null) {
        JsonObject resultJson = resultRawJson.getAsJsonObject();
        String enchIdStr = resultJson.get("enchantment").getAsString();
        ResourceLocation enchId = ResourceLocation.tryParse(enchIdStr);
        if (enchId == null) {
          throw new IllegalArgumentException("Invalid resulting enchantment ID, must be a proper resource location: " + enchIdStr);
        }
        Enchantment result = ForgeRegistries.ENCHANTMENTS.getValue(enchId);
        if (result == null) {
          PELog.LOGGER.warn(PELog.M_SERVER, "Unknown enchantment while parsing recipe for {}: {}", id, enchId);
        } else {
          int level = resultJson.get("level").getAsInt();
          builder.result(result, level);
        }
      }
      return builder.build();
    }
  }

}
