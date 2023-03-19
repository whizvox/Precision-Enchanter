package me.whizvox.precisionenchanter.data.server.compat;

import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import shadows.apotheosis.Apoth;

import java.util.function.Consumer;

public class ApotheosisEnchantmentRecipeProvider extends EnchantmentRecipeProvider {

  public ApotheosisEnchantmentRecipeProvider(DataGenerator gen, String modId) {
    super(gen, modId);
  }

  @Override
  public String getName() {
    return super.getName() + "_Apotheosis";
  }

  @Override
  public void buildRecipes(Consumer<EnchantmentRecipe> output) {
    output.accept(builder(Apoth.Enchantments.ENDLESS_QUIVER.get())
        .ingredient(Apoth.Items.BOW_TOME.get())
        .ingredient(Items.ARROW, 32)
        .cost(9)
        .build());
  }

}
