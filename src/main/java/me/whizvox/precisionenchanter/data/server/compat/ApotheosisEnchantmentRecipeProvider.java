package me.whizvox.precisionenchanter.data.server.compat;

import me.whizvox.precisionenchanter.common.api.condition.ApotheosisModuleCondition;
import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.recipe.ConditionalEnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.Tags;
import shadows.apotheosis.Apoth;
import shadows.apotheosis.adventure.AdventureModule;
import shadows.apotheosis.adventure.loot.LootRarity;

import java.util.function.Consumer;

public class ApotheosisEnchantmentRecipeProvider extends EnchantmentRecipeProvider {

  private static final Condition APOTH_LOADED = Condition.modLoaded("apotheosis");

  public ApotheosisEnchantmentRecipeProvider(DataGenerator gen, String modId) {
    super(gen, modId);
  }

  @Override
  public String getName() {
    return super.getName() + "_Apotheosis";
  }

  public ConditionalEnchantmentRecipe.Builder builder(Enchantment enchantment, int level, String path) {
    return super.builder(enchantment, level, path)
        .condition(APOTH_LOADED);
  }

  @Override
  public void buildRecipes(Consumer<EnchantmentRecipe> output) {
    // Endless Quiver
    output.accept(builder(Apoth.Enchantments.ENDLESS_QUIVER.get())
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Apoth.Items.GEM_DUST.get(), 24)
        .ingredient(Items.ARROW, 64)
        .cost(9)
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());

    // Capturing
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 1)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 4)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 2)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 8)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 3)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 16)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 4)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 32)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 5)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 64)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 6)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 64)
        .ingredient(AdventureModule.RARITY_MATERIALS.get(LootRarity.COMMON), 32)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 7)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 64)
        .ingredient(AdventureModule.RARITY_MATERIALS.get(LootRarity.COMMON), 8)
        .ingredient(AdventureModule.RARITY_MATERIALS.get(LootRarity.UNCOMMON), 4)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 8)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 64)
        .ingredient(AdventureModule.RARITY_MATERIALS.get(LootRarity.UNCOMMON), 8)
        .ingredient(AdventureModule.RARITY_MATERIALS.get(LootRarity.RARE), 4)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 9)
        .ingredient(Items.ENDER_EYE)
        .ingredient(Tags.Items.EGGS, 64)
        .ingredient(AdventureModule.RARITY_MATERIALS.get(LootRarity.RARE), 8)
        .ingredient(AdventureModule.RARITY_MATERIALS.get(LootRarity.EPIC), 4)
        .standardCost()
        .condition(ApotheosisModuleCondition.ADVENTURE)
        .build());

    // 
  }

}
