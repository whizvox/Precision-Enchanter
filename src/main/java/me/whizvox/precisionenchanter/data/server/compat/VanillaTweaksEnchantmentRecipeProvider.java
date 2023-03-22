package me.whizvox.precisionenchanter.data.server.compat;

import io.github.strikerrocker.vt.VanillaTweaks;
import io.github.strikerrocker.vt.enchantments.EnchantmentInit;
import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.recipe.ConditionalEnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class VanillaTweaksEnchantmentRecipeProvider extends EnchantmentRecipeProvider {

  private static final Condition VANILLA_TWEAKS_LOADED = Condition.modLoaded(VanillaTweaks.MOD_ID);

  public VanillaTweaksEnchantmentRecipeProvider(DataGenerator gen, String modId) {
    super(gen, modId);
  }

  @Override
  public String getName() {
    return super.getName() + "_VanillaTweaks";
  }

  // the createPath(Enchantment, int) method throws an exception with this mod because it attempts to check its config
  // when trying to figure out whether to include the level in the name or not, and config files aren't loaded during
  // data generation.

  private ConditionalEnchantmentRecipe.Builder vtbuilder(Enchantment enchantment, int level) {
    return builder(enchantment, level, "vanillatweaks/" + ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath() + "_" + level)
        .condition(VANILLA_TWEAKS_LOADED);
  }

  private ConditionalEnchantmentRecipe.Builder vtbuilder(Enchantment enchantment) {
    return builder(enchantment, 1, "vanillatweaks/" + ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath())
        .condition(VANILLA_TWEAKS_LOADED);
  }

  @Override
  public void buildRecipes(Consumer<EnchantmentRecipe> output) {

    // Blazing
    output.accept(vtbuilder(EnchantmentInit.BLAZING.get())
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Items.BLAST_FURNACE)
        .ingredient(Items.LAVA_BUCKET)
        .ingredient(Items.BLAZE_POWDER, 8)
        .cost(6)
        .build());

    // Homing
    output.accept(vtbuilder(EnchantmentInit.HOMING.get(), 1)
        .ingredient(Items.TARGET)
        .ingredient(Items.SPECTRAL_ARROW)
        .ingredient(Tags.Items.INGOTS_IRON, 4)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 16)
        .cost(2)
        .build());
    output.accept(vtbuilder(EnchantmentInit.HOMING.get(), 2)
        .ingredient(Items.TARGET)
        .ingredient(Items.SPECTRAL_ARROW)
        .ingredient(Tags.Items.INGOTS_IRON, 8)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 32)
        .cost(4)
        .build());
    output.accept(vtbuilder(EnchantmentInit.HOMING.get(), 3)
        .ingredient(Items.TARGET)
        .ingredient(Items.SPECTRAL_ARROW)
        .ingredient(Tags.Items.INGOTS_IRON, 16)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 64)
        .cost(7)
        .build());

    // Hops
    output.accept(vtbuilder(EnchantmentInit.HOPS.get(), 1)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 3)
        .ingredient(Items.RABBIT_FOOT, 3)
        .cost(2)
        .build());
    output.accept(vtbuilder(EnchantmentInit.HOPS.get(), 2)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 6)
        .ingredient(Items.RABBIT_FOOT, 6)
        .cost(5)
        .build());
    output.accept(vtbuilder(EnchantmentInit.HOPS.get(), 3)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 12)
        .ingredient(Items.RABBIT_FOOT, 12)
        .cost(8)
        .build());

    // Nimble
    output.accept(vtbuilder(EnchantmentInit.NIMBLE.get(), 1)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 3)
        .ingredient(Items.SUGAR, 12)
        .cost(2)
        .build());
    output.accept(vtbuilder(EnchantmentInit.NIMBLE.get(), 2)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 6)
        .ingredient(Items.SUGAR, 24)
        .cost(5)
        .build());
    output.accept(vtbuilder(EnchantmentInit.NIMBLE.get(), 3)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 12)
        .ingredient(Items.SUGAR, 48)
        .cost(8)
        .build());

    // Siphon
    output.accept(vtbuilder(EnchantmentInit.SIPHON.get())
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .ingredient(ItemTags.BEDS)
        .cost(6)
        .build());

    // Veteran
    output.accept(vtbuilder(EnchantmentInit.VETERAN.get())
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .ingredient(ItemTags.BEDS)
        .cost(6)
        .build());

    // Vigor
    output.accept(vtbuilder(EnchantmentInit.VIGOR.get(), 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.GLISTERING_MELON_SLICE, 4)
        .cost(1)
        .build());
    output.accept(vtbuilder(EnchantmentInit.VIGOR.get(), 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.GLISTERING_MELON_SLICE, 8)
        .cost(3)
        .build());
    output.accept(vtbuilder(EnchantmentInit.VIGOR.get(), 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.GLISTERING_MELON_SLICE, 16)
        .cost(4)
        .build());
  }

}