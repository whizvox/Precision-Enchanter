package me.whizvox.precisionenchanter.data.server.compat;

import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.recipe.ConditionalEnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.apotheosis.Apoth;

import java.util.function.Consumer;

import static me.whizvox.precisionenchanter.common.api.condition.ApotheosisModuleCondition.*;

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
    // not sure whether to add recipes for boosted enchantments

    // Capturing
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 1)
        .ingredient(Items.LEAD)
        .ingredient(Tags.Items.EGGS, 2)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE)
        .ingredient(Tags.Items.DUSTS_REDSTONE)
        .cost(2)
        .condition(SPAWNER)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 2)
        .ingredient(Items.LEAD)
        .ingredient(Tags.Items.EGGS, 4)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 2)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 2)
        .cost(4)
        .condition(SPAWNER)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 3)
        .ingredient(Items.LEAD)
        .ingredient(Tags.Items.EGGS, 8)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 4)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 4)
        .cost(6)
        .condition(SPAWNER)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 4)
        .ingredient(Items.LEAD)
        .ingredient(Tags.Items.EGGS, 16)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 8)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 8)
        .cost(8)
        .condition(SPAWNER)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 5)
        .ingredient(Items.LEAD)
        .ingredient(Tags.Items.EGGS, 32)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 16)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 16)
        .cost(11)
        .condition(SPAWNER)
        .build());
    output.accept(builder(Apoth.Enchantments.CAPTURING.get(), 6)
        .ingredient(Items.LEAD)
        .ingredient(Tags.Items.EGGS, 64)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 32)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 32)
        .cost(13)
        .condition(SPAWNER)
        .build());

    // Berserker's Fury
    output.accept(builder(Apoth.Enchantments.BERSERKERS_FURY.get(), 1)
        .ingredient(Items.IRON_AXE)
        .ingredient(Items.CHAIN, 12)
        .ingredient(Items.PAPER, 8)
        .cost(3)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.BERSERKERS_FURY.get(), 2)
        .ingredient(Items.IRON_AXE)
        .ingredient(Items.CHAIN, 24)
        .ingredient(Items.PAPER, 16)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.BERSERKERS_FURY.get(), 3)
        .ingredient(Items.IRON_AXE)
        .ingredient(Items.CHAIN, 48)
        .ingredient(Items.PAPER, 32)
        .cost(9)
        .condition(ENCHANTING)
        .build());

    // Boon of the Earth
    output.accept(builder(Apoth.Enchantments.EARTHS_BOON.get(), 1)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Items.HEART_OF_THE_SEA)
        .ingredient(Tags.Items.STONE, 4)
        .ingredient(Tags.Items.GEMS_LAPIS, 4)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.EARTHS_BOON.get(), 2)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Items.HEART_OF_THE_SEA)
        .ingredient(Tags.Items.STONE, 8)
        .ingredient(Tags.Items.GEMS_LAPIS, 8)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.EARTHS_BOON.get(), 3)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Items.HEART_OF_THE_SEA)
        .ingredient(Tags.Items.STONE, 16)
        .ingredient(Tags.Items.GEMS_LAPIS, 16)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.EARTHS_BOON.get(), 4)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Items.HEART_OF_THE_SEA)
        .ingredient(Tags.Items.STONE, 32)
        .ingredient(Tags.Items.GEMS_LAPIS, 32)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.EARTHS_BOON.get(), 5)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Items.HEART_OF_THE_SEA)
        .ingredient(Tags.Items.STONE, 64)
        .ingredient(Tags.Items.GEMS_LAPIS, 64)
        .cost(11)
        .condition(ENCHANTING)
        .build());

    // Chainsaw
    output.accept(builder(Apoth.Enchantments.CHAINSAW.get())
        .ingredient(Items.NETHERITE_AXE)
        .ingredient(ItemTags.LOGS, 64)
        .ingredient(Items.CHAIN, 8)
        .cost(8)
        .condition(ENCHANTING)
        .build());

    // Chromatic Aberration
    output.accept(builder(Apoth.Enchantments.CHROMATIC.get())
        .ingredient(Items.SHEARS)
        .ingredient(Tags.Items.DYES_RED, 16)
        .ingredient(Tags.Items.DYES_GREEN, 16)
        .ingredient(Tags.Items.DYES_BLUE, 16)
        .cost(5)
        .condition(ENCHANTING)
        .build());

    // Crescendo of Bolts
    output.accept(builder(Apoth.Enchantments.CRESCENDO.get(), 1)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.CROSSBOW)
        .ingredient(Items.ARROW, 4)
        .ingredient(Items.ENDER_EYE, 3)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.CRESCENDO.get(), 2)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.CROSSBOW)
        .ingredient(Items.ARROW, 8)
        .ingredient(Items.ENDER_EYE, 6)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.CRESCENDO.get(), 3)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.CROSSBOW)
        .ingredient(Items.ARROW, 16)
        .ingredient(Items.ENDER_EYE, 12)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.CRESCENDO.get(), 4)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.CROSSBOW)
        .ingredient(Items.ARROW, 32)
        .ingredient(Items.ENDER_EYE, 24)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.CRESCENDO.get(), 5)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.CROSSBOW)
        .ingredient(Items.ARROW, 64)
        .ingredient(Items.ENDER_EYE, 48)
        .cost(11)
        .condition(ENCHANTING)
        .build());

    // Endless Quiver
    output.accept(builder(Apoth.Enchantments.ENDLESS_QUIVER.get())
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Apoth.Items.GEM_DUST.get(), 12)
        .ingredient(Items.ARROW, 64)
        .cost(9)
        .condition(ENCHANTING)
        .condition(ADVENTURE)
        .build());
    output.accept(builder(Apoth.Enchantments.ENDLESS_QUIVER.get(), 1, "apotheosis/endless_quiver_alt")
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.GLOWSTONE_DUST, 24)
        .ingredient(Items.ARROW, 64)
        .cost(9)
        .condition(ENCHANTING)
        .condition(Condition.not(ADVENTURE))
        .build());

    // Growth Serum
    output.accept(builder(Apoth.Enchantments.GROWTH_SERUM.get())
        .ingredient(Items.SHEARS)
        .ingredient(ItemTags.WOOL, 32)
        .ingredient(Items.BONE_MEAL, 12)
        .ingredient(Items.MOSS_BLOCK, 12)
        .cost(5)
        .condition(ENCHANTING)
        .build());

    // Icy Thorns
    Enchantment ICY_THORNS = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("apotheosis", "icy_thorns"));
    output.accept(builder(ICY_THORNS, 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.CACTUS, 4)
        .ingredient(Items.ICE, 16)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(ICY_THORNS, 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.CACTUS, 8)
        .ingredient(Items.ICE, 32)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(ICY_THORNS, 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.CACTUS, 16)
        .ingredient(Items.ICE, 64)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(ICY_THORNS, 4)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.CACTUS, 32)
        .ingredient(Items.PACKED_ICE, 14)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(ICY_THORNS, 5)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.CACTUS, 64)
        .ingredient(Items.PACKED_ICE, 28)
        .cost(11)
        .condition(ENCHANTING)
        .build());

    // Knowledge of the Ages
    output.accept(builder(Apoth.Enchantments.KNOWLEDGE.get(), 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.EXPERIENCE_BOTTLE, 4)
        .ingredient(Tags.Items.GEMS_LAPIS, 8)
        .cost(3)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.KNOWLEDGE.get(), 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.EXPERIENCE_BOTTLE, 8)
        .ingredient(Tags.Items.GEMS_LAPIS, 16)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.KNOWLEDGE.get(), 3)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.EXPERIENCE_BOTTLE, 16)
        .ingredient(Tags.Items.GEMS_LAPIS, 32)
        .cost(9)
        .condition(ENCHANTING)
        .build());

    // Life-Mending
    output.accept(builder(Apoth.Enchantments.LIFE_MENDING.get(), 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(ItemTags.ANVIL)
        .ingredient(Items.GHAST_TEAR, 6)
        .cost(3)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.LIFE_MENDING.get(), 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(ItemTags.ANVIL)
        .ingredient(Items.GHAST_TEAR, 12)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.LIFE_MENDING.get(), 3)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(ItemTags.ANVIL)
        .ingredient(Items.GHAST_TEAR, 24)
        .cost(9)
        .condition(ENCHANTING)
        .build());

    // Miner's Fervor
    output.accept(builder(Apoth.Enchantments.MINERS_FERVOR.get(), 1)
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 2)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.MINERS_FERVOR.get(), 2)
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 2)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 4)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.MINERS_FERVOR.get(), 3)
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 4)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 8)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.MINERS_FERVOR.get(), 4)
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 8)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 16)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.MINERS_FERVOR.get(), 5)
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 16)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 32)
        .cost(10)
        .condition(ENCHANTING)
        .build());

    // Nature's Blessing
    output.accept(builder(Apoth.Enchantments.NATURES_BLESSING.get(), 1)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.BONE_MEAL, 4)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.NATURES_BLESSING.get(), 2)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.BONE_MEAL, 8)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.NATURES_BLESSING.get(), 3)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.BONE_MEAL, 16)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.NATURES_BLESSING.get(), 4)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.BONE_MEAL, 32)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.NATURES_BLESSING.get(), 5)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.BONE_MEAL, 64)
        .cost(10)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.NATURES_BLESSING.get(), 6)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.BONE_BLOCK, 14)
        .cost(12)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.NATURES_BLESSING.get(), 7)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.BONE_BLOCK, 28)
        .cost(14)
        .condition(ENCHANTING)
        .build());

    // Obliteration
    output.accept(builder(Apoth.Enchantments.OBLITERATION.get())
        .ingredient(ItemTags.ANVIL)
        .ingredient(Items.IRON_AXE)
        .ingredient(Items.EXPERIENCE_BOTTLE)
        .cost(8)
        .condition(ENCHANTING)
        .build());

    // Rebounding
    final Enchantment REBOUNDING = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("apotheosis", "rebounding"));
    output.accept(builder(REBOUNDING, 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PISTON)
        .ingredient(Items.SLIME_BLOCK, 4)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(REBOUNDING, 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PISTON)
        .ingredient(Items.SLIME_BLOCK, 8)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(REBOUNDING, 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PISTON)
        .ingredient(Items.SLIME_BLOCK, 16)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(REBOUNDING, 4)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PISTON)
        .ingredient(Items.SLIME_BLOCK, 32)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(REBOUNDING, 5)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PISTON)
        .ingredient(Items.SLIME_BLOCK, 64)
        .cost(11)
        .condition(ENCHANTING)
        .build());

    // Reflective Defenses
    output.accept(builder(Apoth.Enchantments.REFLECTIVE.get(), 1)
        .ingredient(Items.SHIELD)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 8)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.REFLECTIVE.get(), 2)
        .ingredient(Items.SHIELD)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.REFLECTIVE.get(), 3)
        .ingredient(Items.SHIELD)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.REFLECTIVE.get(), 4)
        .ingredient(Items.SHIELD)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.REFLECTIVE.get(), 5)
        .ingredient(Items.SHIELD)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 32)
        .cost(11)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.REFLECTIVE.get(), 6)
        .ingredient(Items.SHIELD)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .cost(13)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.REFLECTIVE.get(), 7)
        .ingredient(Items.SHIELD)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .cost(15)
        .condition(ENCHANTING)
        .build());

    // Scavenger
    output.accept(builder(Apoth.Enchantments.SCAVENGER.get(), 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.ROTTEN_FLESH, 16)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 8)
        .cost(3)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SCAVENGER.get(), 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.ROTTEN_FLESH, 32)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 16)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SCAVENGER.get(), 3)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.ROTTEN_FLESH, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 32)
        .cost(10)
        .condition(ENCHANTING)
        .build());

    // Shield Bash
    final Enchantment SHIELD_BASH = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("apotheosis", "shield_bash"));
    output.accept(builder(SHIELD_BASH, 1)
        .ingredient(Items.SHIELD)
        .ingredient(Items.DIAMOND_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 8)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(SHIELD_BASH, 2)
        .ingredient(Items.SHIELD)
        .ingredient(Items.DIAMOND_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(SHIELD_BASH, 3)
        .ingredient(Items.SHIELD)
        .ingredient(Items.DIAMOND_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(SHIELD_BASH, 4)
        .ingredient(Items.SHIELD)
        .ingredient(Items.DIAMOND_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .cost(8)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(SHIELD_BASH, 5)
        .ingredient(Items.SHIELD)
        .ingredient(Items.DIAMOND_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 32)
        .cost(11)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(SHIELD_BASH, 6)
        .ingredient(Items.SHIELD)
        .ingredient(Items.DIAMOND_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .cost(13)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(SHIELD_BASH, 7)
        .ingredient(Items.SHIELD)
        .ingredient(Items.DIAMOND_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .cost(15)
        .condition(ENCHANTING)
        .build());

    // Spearfishing
    output.accept(builder(Apoth.Enchantments.SPEARFISHING.get(), 1)
        .ingredient(Items.FISHING_ROD)
        .ingredient(ItemTags.FISHES)
        .ingredient(Tags.Items.GEMS_PRISMARINE)
        .cost(1)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SPEARFISHING.get(), 2)
        .ingredient(Items.FISHING_ROD)
        .ingredient(ItemTags.FISHES, 2)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 2)
        .cost(2)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SPEARFISHING.get(), 3)
        .ingredient(Items.FISHING_ROD)
        .ingredient(ItemTags.FISHES, 4)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 4)
        .cost(3)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SPEARFISHING.get(), 4)
        .ingredient(Items.FISHING_ROD)
        .ingredient(ItemTags.FISHES, 8)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 8)
        .cost(4)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SPEARFISHING.get(), 5)
        .ingredient(Items.FISHING_ROD)
        .ingredient(ItemTags.FISHES, 16)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 16)
        .cost(5)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SPEARFISHING.get(), 6)
        .ingredient(Items.FISHING_ROD)
        .ingredient(ItemTags.FISHES, 32)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 32)
        .cost(6)
        .condition(ENCHANTING)
        .build());
    output.accept(builder(Apoth.Enchantments.SPEARFISHING.get(), 7)
        .ingredient(Items.FISHING_ROD)
        .ingredient(ItemTags.FISHES, 64)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 64)
        .cost(8)
        .condition(ENCHANTING)
        .build());

    // Splitting
    output.accept(builder(Apoth.Enchantments.SPLITTING.get())
        .ingredient(ItemTags.ANVIL)
        .ingredient(Items.GOLDEN_AXE)
        .ingredient(Items.EXPERIENCE_BOTTLE)
        .cost(8)
        .condition(ENCHANTING)
        .build());

    // Stable Footing
    output.accept(builder(Apoth.Enchantments.STABLE_FOOTING.get())
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Tags.Items.FEATHERS, 32)
        .ingredient(Items.SUGAR, 32)
        .cost(6)
        .condition(ENCHANTING)
        .build());

    // Temptation
    output.accept(builder(Apoth.Enchantments.TEMPTING.get())
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Items.CARROT_ON_A_STICK)
        .ingredient(Items.WHEAT, 24)
        .ingredient(Tags.Items.SEEDS, 32)
        .cost(5)
        .condition(ENCHANTING)
        .build());

    // Worker Exploitation
    output.accept(builder(Apoth.Enchantments.EXPLOITATION.get())
        .ingredient(Items.SHEARS)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(ItemTags.WOOL, 32)
        .ingredient(Items.MUTTON, 10)
        .cost(5)
        .condition(ENCHANTING)
        .build());
  }

}
