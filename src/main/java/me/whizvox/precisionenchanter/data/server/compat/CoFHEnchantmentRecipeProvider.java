package me.whizvox.precisionenchanter.data.server.compat;

import cofh.core.init.CoreEnchantments;
import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.recipe.ConditionalEnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CoFHEnchantmentRecipeProvider extends EnchantmentRecipeProvider {

  protected static final Condition COFH_LOADED = Condition.modLoaded("cofh_core");

  public CoFHEnchantmentRecipeProvider(DataGenerator gen, String modId) {
    super(gen, modId);
  }

  @Override
  public String getName() {
    return super.getName() + "_CoFHCore";
  }

  @Override
  public ConditionalEnchantmentRecipe.Builder builder(Enchantment result, int level, String path) {
    return super.builder(result, level, path).condition(Condition.and(COFH_LOADED, Condition.cofhEnchantmentEnabled(result)));
  }

  @Override
  public void buildRecipes(Consumer<EnchantmentRecipe> output) {
    // Holding
    output.accept(builder(CoreEnchantments.HOLDING.get(), 1)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 4)
        .ingredient(Tags.Items.OBSIDIAN, 4)
        .cost(2)
        .build());
    output.accept(builder(CoreEnchantments.HOLDING.get(), 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 8)
        .ingredient(Tags.Items.OBSIDIAN, 8)
        .cost(5)
        .build());
    output.accept(builder(CoreEnchantments.HOLDING.get(), 3)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 16)
        .ingredient(Tags.Items.OBSIDIAN, 16)
        .cost(7)
        .build());
    output.accept(builder(CoreEnchantments.HOLDING.get(), 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 16)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 32)
        .ingredient(Tags.Items.OBSIDIAN, 32)
        .cost(10)
        .build());
  }

}
