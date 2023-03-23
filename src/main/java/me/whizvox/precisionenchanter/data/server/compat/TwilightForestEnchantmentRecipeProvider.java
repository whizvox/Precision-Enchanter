package me.whizvox.precisionenchanter.data.server.compat;

import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.recipe.ConditionalEnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import twilightforest.TwilightForestMod;
import twilightforest.enchantment.TFEnchantments;
import twilightforest.item.TFItems;

import java.util.function.Consumer;

public class TwilightForestEnchantmentRecipeProvider extends EnchantmentRecipeProvider {

  private static final Condition TWILIGHT_FOREST_LOADED = Condition.modLoaded(TwilightForestMod.ID);

  public TwilightForestEnchantmentRecipeProvider(DataGenerator gen, String modId) {
    super(gen, modId);
  }

  @Override
  public String getName() {
    return super.getName() + "_TwilightForest";
  }

  @Override
  public ConditionalEnchantmentRecipe.Builder builder(Enchantment result, int level, String path) {
    return super.builder(result, level, path).condition(TWILIGHT_FOREST_LOADED);
  }

  @Override
  public void buildRecipes(Consumer<EnchantmentRecipe> output) {

    // Chill Aura
    output.accept(builder(TFEnchantments.CHILL_AURA.get(), 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.ICE, 6)
        .ingredient(TFItems.ALPHA_YETI_FUR.get(), 2)
        .standardCost()
        .build());

    output.accept(builder(TFEnchantments.CHILL_AURA.get(), 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PACKED_ICE, 6)
        .ingredient(TFItems.ALPHA_YETI_FUR.get(), 4)
        .standardCost()
        .build());

    output.accept(builder(TFEnchantments.CHILL_AURA.get(), 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.BLUE_ICE, 6)
        .ingredient(TFItems.ALPHA_YETI_FUR.get(), 8)
        .standardCost()
        .build());

    // Fire React
    output.accept(builder(TFEnchantments.FIRE_REACT.get(), 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.MAGMA_BLOCK, 5)
        .ingredient(TFItems.FIERY_INGOT.get(), 8)
        .standardCost()
        .build());

    output.accept(builder(TFEnchantments.FIRE_REACT.get(), 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.MAGMA_BLOCK, 10)
        .ingredient(TFItems.FIERY_INGOT.get(), 16)
        .standardCost()
        .build());

    output.accept(builder(TFEnchantments.FIRE_REACT.get(), 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.MAGMA_BLOCK, 20)
        .ingredient(TFItems.FIERY_INGOT.get(), 32)
        .standardCost()
        .build());

    // Destruction
    output.accept(builder(TFEnchantments.DESTRUCTION.get(), 1)
        .ingredient(Items.IRON_BLOCK)
        .ingredient(Items.WOODEN_PICKAXE)
        .ingredient(TFItems.KNIGHTMETAL_INGOT.get(), 5)
        .cost(1)
        .build());

    output.accept(builder(TFEnchantments.DESTRUCTION.get(), 2)
        .ingredient(Items.IRON_BLOCK)
        .ingredient(Items.STONE_PICKAXE)
        .ingredient(TFItems.KNIGHTMETAL_INGOT.get(), 10)
        .cost(3)
        .build());

    output.accept(builder(TFEnchantments.DESTRUCTION.get(), 3)
        .ingredient(Items.IRON_BLOCK)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(TFItems.KNIGHTMETAL_INGOT.get(), 20)
        .cost(5)
        .build());

    // TODO Add recipes for Preservation and Block Strength

  }

}
