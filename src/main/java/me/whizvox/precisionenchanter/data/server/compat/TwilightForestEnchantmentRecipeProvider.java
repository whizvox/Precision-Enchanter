package me.whizvox.precisionenchanter.data.server.compat;

import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.recipe.ConditionalEnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
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
    final Item KNIGHTMETAL_BLOCK = ForgeRegistries.ITEMS.getValue(TFBlocks.KNIGHTMETAL_BLOCK.getId());
    output.accept(builder(TFEnchantments.DESTRUCTION.get(), 1)
        .ingredient(KNIGHTMETAL_BLOCK)
        .ingredient(Items.IRON_SHOVEL)
        .cost(3)
        .build());

    output.accept(builder(TFEnchantments.DESTRUCTION.get(), 2)
        .ingredient(KNIGHTMETAL_BLOCK)
        .ingredient(Items.DIAMOND_SHOVEL)
        .cost(6)
        .build());

    output.accept(builder(TFEnchantments.DESTRUCTION.get(), 3)
        .ingredient(KNIGHTMETAL_BLOCK)
        .ingredient(Items.NETHERITE_SHOVEL)
        .cost(10)
        .build());

    // Block Strength
    output.accept(builder(TFEnchantments.BLOCK_STRENGTH.get(), 1)
        .ingredient(KNIGHTMETAL_BLOCK)
        .ingredient(TFItems.KNIGHTMETAL_PICKAXE.get())
        .ingredient(Tags.Items.INGOTS_IRON, 3)
        .cost(2)
        .build());
    output.accept(builder(TFEnchantments.BLOCK_STRENGTH.get(), 2)
        .ingredient(KNIGHTMETAL_BLOCK)
        .ingredient(TFItems.KNIGHTMETAL_PICKAXE.get())
        .ingredient(Tags.Items.GEMS_DIAMOND, 3)
        .cost(4)
        .build());
    output.accept(builder(TFEnchantments.BLOCK_STRENGTH.get(), 3)
        .ingredient(KNIGHTMETAL_BLOCK)
        .ingredient(TFItems.KNIGHTMETAL_PICKAXE.get())
        .ingredient(Tags.Items.INGOTS_NETHERITE)
        .cost(7)
        .build());

    // Preservation
    output.accept(builder(TFEnchantments.PRESERVATION.get())
        .ingredient(KNIGHTMETAL_BLOCK)
        .ingredient(ItemTags.WOOL, 32)
        .ingredient(Items.SLIME_BLOCK, 12)
        .cost(4)
        .build());

  }

}
