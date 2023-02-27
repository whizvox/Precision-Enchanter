package me.whizvox.precisionenchanter.data.server.compat;

import cofh.core.init.CoreEnchantments;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CoFHCoreEnchantmentRecipeProvider extends EnchantmentRecipeProvider {

  public CoFHCoreEnchantmentRecipeProvider(DataGenerator gen, String modId) {
    super(gen, modId);
  }

  @Override
  public String getName() {
    return super.getName() + "CoFHCore";
  }

  @Override
  public void buildRecipes(Consumer<EnchantmentRecipe> output) {
    // Holding
    output.accept(builder(CoreEnchantments.HOLDING.get(), 1)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 4)
        .ingredient(Tags.Items.OBSIDIAN, 4)
        .cost(3)
        .build());
    output.accept(builder(CoreEnchantments.HOLDING.get(), 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 8)
        .ingredient(Tags.Items.OBSIDIAN, 8)
        .cost(6)
        .build());
    output.accept(builder(CoreEnchantments.HOLDING.get(), 3)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 16)
        .ingredient(Tags.Items.OBSIDIAN, 16)
        .cost(9)
        .build());
  }

}
