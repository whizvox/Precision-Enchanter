package me.whizvox.precisionenchanter.data.server.compat;

import cofh.ensorcellation.init.EnsorcEnchantments;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.recipe.ConditionalEnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class EnsorcellationEnchantmentRecipeProvider extends EnchantmentRecipeProvider {

  private static final Condition ENSORC_LOADED = Condition.modLoaded("ensorcellation");

  public EnsorcellationEnchantmentRecipeProvider(DataGenerator gen, String modId) {
    super(gen, modId);
  }

  @Override
  public String getName() {
    return super.getName() + "_Ensorcellation";
  }

  @Override
  public ConditionalEnchantmentRecipe.Builder builder(Enchantment result, int level, String path) {
    return super.builder(result, level, path)
        .condition(ENSORC_LOADED)
        .condition(Condition.cofhEnchantmentEnabled(result));
  }

  @Override
  public void buildRecipes(Consumer<EnchantmentRecipe> output) {
    // Soulbound
    output.accept(builder(EnsorcEnchantments.SOULBOUND.get())
        .ingredient(Items.SOUL_LANTERN)
        .ingredient(Items.RESPAWN_ANCHOR)
        .ingredient(Items.CHAIN, 16)
        .cost(5)
        .build());

    // Magic Protection
    output.accept(builder(EnsorcEnchantments.PROTECTION_MAGIC.get(), 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Tags.Items.INGOTS_COPPER)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE)
        .cost(1)
        .build());
    output.accept(builder(EnsorcEnchantments.PROTECTION_MAGIC.get(), 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Tags.Items.INGOTS_COPPER, 2)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 2)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.PROTECTION_MAGIC.get(), 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Tags.Items.INGOTS_COPPER, 4)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 4)
        .cost(3)
        .build());
    output.accept(builder(EnsorcEnchantments.PROTECTION_MAGIC.get(), 4)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Tags.Items.INGOTS_COPPER, 8)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 8)
        .cost(5)
        .build());

    // Displacement
    output.accept(builder(EnsorcEnchantments.DISPLACEMENT.get(), 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Tags.Items.ENDER_PEARLS, 4)
        .ingredient(Items.CHORUS_FRUIT, 8)
        .cost(2)
        .build());

    output.accept(builder(EnsorcEnchantments.DISPLACEMENT.get(), 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Tags.Items.ENDER_PEARLS, 8)
        .ingredient(Items.CHORUS_FRUIT, 16)
        .cost(4)
        .build());

    output.accept(builder(EnsorcEnchantments.DISPLACEMENT.get(), 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Tags.Items.ENDER_PEARLS, 16)
        .ingredient(Items.CHORUS_FRUIT, 32)
        .cost(7)
        .build());

    // Flaming Rebuke
    output.accept(builder(EnsorcEnchantments.FIRE_REBUKE.get(), 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.FIRE_CHARGE, 6)
        .ingredient(Items.PISTON, 2)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.FIRE_REBUKE.get(), 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.FIRE_CHARGE, 12)
        .ingredient(Items.PISTON, 4)
        .cost(4)
        .build());
    output.accept(builder(EnsorcEnchantments.FIRE_REBUKE.get(), 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.FIRE_CHARGE, 24)
        .ingredient(Items.PISTON, 8)
        .cost(7)
        .build());

    // Chilling Rebuke
    output.accept(builder(EnsorcEnchantments.FROST_REBUKE.get(), 1)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PACKED_ICE, 3)
        .ingredient(Items.PISTON, 2)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.FROST_REBUKE.get(), 2)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PACKED_ICE, 6)
        .ingredient(Items.PISTON, 4)
        .cost(4)
        .build());
    output.accept(builder(EnsorcEnchantments.FROST_REBUKE.get(), 3)
        .ingredient(Items.GOLDEN_CHESTPLATE)
        .ingredient(Items.PACKED_ICE, 12)
        .ingredient(Items.PISTON, 8)
        .cost(7)
        .build());

    // Reach
    output.accept(builder(EnsorcEnchantments.REACH.get(), 1)
        .ingredient(Items.LEATHER_CHESTPLATE)
        .ingredient(Items.PISTON, 2)
        .ingredient(Tags.Items.INGOTS_IRON, 16)
        .cost(3)
        .build());
    output.accept(builder(EnsorcEnchantments.REACH.get(), 2)
        .ingredient(Items.LEATHER_CHESTPLATE)
        .ingredient(Items.PISTON, 4)
        .ingredient(Tags.Items.INGOTS_IRON, 32)
        .cost(6)
        .build());
    output.accept(builder(EnsorcEnchantments.REACH.get(), 3)
        .ingredient(Items.LEATHER_CHESTPLATE)
        .ingredient(Items.PISTON, 8)
        .ingredient(Tags.Items.INGOTS_IRON, 64)
        .cost(9)
        .build());

    // Vitality
    output.accept(builder(EnsorcEnchantments.VITALITY.get(), 1)
        .ingredient(Items.IRON_CHESTPLATE)
        .ingredient(Items.SOUL_LANTERN)
        .ingredient(Items.GLISTERING_MELON_SLICE, 8)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.VITALITY.get(), 2)
        .ingredient(Items.IRON_CHESTPLATE)
        .ingredient(Items.SOUL_LANTERN)
        .ingredient(Items.GLISTERING_MELON_SLICE, 16)
        .cost(4)
        .build());
    output.accept(builder(EnsorcEnchantments.VITALITY.get(), 3)
        .ingredient(Items.IRON_CHESTPLATE)
        .ingredient(Items.SOUL_LANTERN)
        .ingredient(Items.GLISTERING_MELON_SLICE, 32)
        .cost(6)
        .build());

    // Air Affinity
    output.accept(builder(EnsorcEnchantments.AIR_AFFINITY.get())
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.FEATHERS, 32)
        .ingredient(Items.SUGAR, 32)
        .cost(6)
        .build());

    // Gourmand
    output.accept(builder(EnsorcEnchantments.GOURMAND.get(), 1)
        .ingredient(Items.GOLDEN_HELMET)
        .ingredient(Items.COOKED_BEEF)
        .ingredient(Items.BAKED_POTATO)
        .ingredient(Tags.Items.MUSHROOMS)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.GOURMAND.get(), 2)
        .ingredient(Items.GOLDEN_HELMET)
        .ingredient(Items.CAKE)
        .ingredient(Items.RABBIT_STEW)
        .ingredient(Items.GOLDEN_APPLE)
        .standardCost()
        .build());

    // Insight
    output.accept(builder(EnsorcEnchantments.XP_BOOST.get(), 1)
        .ingredient(Items.GOLDEN_HELMET)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Tags.Items.HEADS)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.XP_BOOST.get(), 2)
        .ingredient(Items.GOLDEN_HELMET)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Tags.Items.HEADS, 2)
        .cost(5)
        .build());
    output.accept(builder(EnsorcEnchantments.XP_BOOST.get(), 3)
        .ingredient(Items.GOLDEN_HELMET)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.GOLDEN_HOE)
        .ingredient(Tags.Items.HEADS, 4)
        .cost(7)
        .build());

    // Cavalier
    output.accept(builder(EnsorcEnchantments.CAVALIER.get(), 1)
        .ingredient(Items.LEATHER_HORSE_ARMOR)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.CAVALIER.get(), 2)
        .ingredient(Items.IRON_HORSE_ARMOR)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.CAVALIER.get(), 3)
        .ingredient(Items.GOLDEN_HORSE_ARMOR)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.CAVALIER.get(), 4)
        .ingredient(Items.DIAMOND_HORSE_ARMOR)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 32)
        .standardCost()
        .build());

    // Ender Disruption
    output.accept(builder(EnsorcEnchantments.DAMAGE_ENDER.get(), 1)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.ENDER_PEARLS)
        .ingredient(Items.CHAIN, 4)
        .cost(1)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ENDER.get(), 2)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.ENDER_PEARLS, 2)
        .ingredient(Items.CHAIN, 8)
        .cost(3)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ENDER.get(), 3)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.ENDER_PEARLS, 4)
        .ingredient(Items.CHAIN, 16)
        .cost(5)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ENDER.get(), 4)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.ENDER_PEARLS, 8)
        .ingredient(Items.CHAIN, 32)
        .cost(7)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ENDER.get(), 5)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.ENDER_PEARLS, 16)
        .ingredient(Items.CHAIN, 64)
        .cost(9)
        .build());

    // Frost Aspect
    output.accept(builder(EnsorcEnchantments.FROST_ASPECT.get(), 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.PACKED_ICE, 8)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.FROST_ASPECT.get(), 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.PACKED_ICE, 12)
        .ingredient(Items.POWDER_SNOW_BUCKET)
        .standardCost()
        .build());

    // Leech
    output.accept(builder(EnsorcEnchantments.LEECH.get(), 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.GLISTERING_MELON_SLICE, 2)
        .ingredient(Tags.Items.BONES, 16)
        .ingredient(Items.ROTTEN_FLESH, 16)
        .standardCost()
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.LEECH.get(), 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.GLISTERING_MELON_SLICE, 4)
        .ingredient(Tags.Items.BONES, 32)
        .ingredient(Items.ROTTEN_FLESH, 32)
        .standardCost()
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.LEECH.get(), 3)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.GLISTERING_MELON_SLICE, 8)
        .ingredient(Tags.Items.HEADS, 4)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.LEECH.get(), 4)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Items.GLISTERING_MELON_SLICE, 16)
        .ingredient(Tags.Items.HEADS, 8)
        .standardCost()
        .build());

    // Instigating
    output.accept(builder(EnsorcEnchantments.INSTIGATING.get())
        .ingredient(Items.DIAMOND_AXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON)
        .ingredient(Items.FERMENTED_SPIDER_EYE, 3)
        .cost(4)
        .build());

    // Magic Edge
    output.accept(builder(EnsorcEnchantments.MAGIC_EDGE.get(), 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .ingredient(Tags.Items.GEMS_LAPIS, 4)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.MAGIC_EDGE.get(), 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .ingredient(Tags.Items.GEMS_LAPIS, 8)
        .cost(4)
        .build());
    output.accept(builder(EnsorcEnchantments.MAGIC_EDGE.get(), 3)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .ingredient(Tags.Items.GEMS_LAPIS, 16)
        .cost(7)
        .build());

    // Outlaw
    output.accept(builder(EnsorcEnchantments.DAMAGE_VILLAGER.get(), 1)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_IRON, 3)
        .ingredient(Tags.Items.GEMS_EMERALD)
        .cost(1)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_VILLAGER.get(), 2)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_IRON, 6)
        .ingredient(Tags.Items.GEMS_EMERALD, 2)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_VILLAGER.get(), 3)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_IRON, 12)
        .ingredient(Tags.Items.GEMS_EMERALD, 4)
        .cost(4)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_VILLAGER.get(), 4)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_IRON, 24)
        .ingredient(Tags.Items.GEMS_EMERALD, 8)
        .cost(5)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_VILLAGER.get(), 5)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_IRON, 48)
        .ingredient(Tags.Items.GEMS_EMERALD, 16)
        .cost(7)
        .build());

    // Vigilante
    output.accept(builder(EnsorcEnchantments.DAMAGE_ILLAGER.get(), 1)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_COPPER, 3)
        .ingredient(Tags.Items.GEMS_EMERALD)
        .cost(1)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ILLAGER.get(), 2)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_COPPER, 6)
        .ingredient(Tags.Items.GEMS_EMERALD, 2)
        .cost(2)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ILLAGER.get(), 3)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_COPPER, 12)
        .ingredient(Tags.Items.GEMS_EMERALD, 4)
        .cost(4)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ILLAGER.get(), 4)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_COPPER, 24)
        .ingredient(Tags.Items.GEMS_EMERALD, 8)
        .cost(5)
        .build());
    output.accept(builder(EnsorcEnchantments.DAMAGE_ILLAGER.get(), 5)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.INGOTS_COPPER, 48)
        .ingredient(Tags.Items.GEMS_EMERALD, 16)
        .cost(7)
        .build());

    // Vorpal
    output.accept(builder(EnsorcEnchantments.VORPAL.get(), 1)
        .ingredient(Items.IRON_AXE)
        .ingredient(Items.RABBIT_FOOT)
        .ingredient(Tags.Items.GEMS_LAPIS, 16)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.VORPAL.get(), 2)
        .ingredient(Items.IRON_AXE)
        .ingredient(Items.RABBIT_FOOT, 2)
        .ingredient(Tags.Items.GEMS_LAPIS, 32)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.VORPAL.get(), 3)
        .ingredient(Items.IRON_AXE)
        .ingredient(Items.RABBIT_FOOT, 4)
        .ingredient(Tags.Items.GEMS_LAPIS, 64)
        .standardCost()
        .build());

    // Excavating
    output.accept(builder(EnsorcEnchantments.EXCAVATING.get())
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Items.DIAMOND_AXE)
        .ingredient(Items.DIAMOND_SHOVEL)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON)
        .cost(6)
        .build());

    // Hunter's Bounty
    output.accept(builder(EnsorcEnchantments.HUNTER.get(), 1)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.GEMS_LAPIS, 30)
        .ingredient(Items.RABBIT_FOOT)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.HUNTER.get(), 2)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.GEMS_LAPIS, 60)
        .ingredient(Items.HEART_OF_THE_SEA)
        .standardCost()
        .build());

    // Quick Draw
    output.accept(builder(EnsorcEnchantments.QUICK_DRAW.get(), 1)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.SUGAR, 16)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.QUICK_DRAW.get(), 2)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.SUGAR, 32)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.QUICK_DRAW.get(), 3)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.SUGAR, 64)
        .standardCost()
        .build());

    // Trueshot
    output.accept(builder(EnsorcEnchantments.TRUESHOT.get(), 1)
        .ingredient(Items.TARGET)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.FEATHERS, 8)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.TRUESHOT.get(), 2)
        .ingredient(Items.TARGET)
        .ingredient(Items.DISPENSER, 2)
        .ingredient(Tags.Items.FEATHERS, 16)
        .standardCost()
        .build());

    // Volley
    output.accept(builder(EnsorcEnchantments.VOLLEY.get())
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Items.ARROW, 32)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .cost(6)
        .build());

    // Angler's Bounty
    output.accept(builder(EnsorcEnchantments.ANGLER.get(), 1)
        .ingredient(Items.FISHING_ROD)
        .ingredient(Tags.Items.GEMS_LAPIS, 24)
        .ingredient(Items.RABBIT_FOOT)
        .cost(3)
        .build());
    output.accept(builder(EnsorcEnchantments.ANGLER.get(), 2)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.GEMS_LAPIS, 48)
        .ingredient(Items.HEART_OF_THE_SEA)
        .cost(6)
        .build());

    // Pilfering
    output.accept(builder(EnsorcEnchantments.PILFERING.get())
        .ingredient(Items.FISHING_ROD)
        .ingredient(Tags.Items.NUGGETS_IRON, 16)
        .ingredient(Tags.Items.NUGGETS_GOLD, 16)
        .ingredient(Tags.Items.GEMS_LAPIS, 7)
        .cost(4)
        .build());

    // Bulwark
    output.accept(builder(EnsorcEnchantments.BULWARK.get())
        .ingredient(Items.SHIELD)
        .ingredient(ItemTags.ANVIL)
        .ingredient(Items.CHAIN, 16)
        .cost(5)
        .build());

    // Phalanx
    output.accept(builder(EnsorcEnchantments.PHALANX.get(), 1)
        .ingredient(Items.SHIELD)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Items.SUGAR, 16)
        .ingredient(Tags.Items.FEATHERS, 8)
        .standardCost()
        .build());
    output.accept(builder(EnsorcEnchantments.PHALANX.get(), 2)
        .ingredient(Items.SHIELD)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Items.SUGAR, 32)
        .ingredient(Tags.Items.FEATHERS, 16)
        .standardCost()
        .build());

    // Curse of Foolishness
    output.accept(builder(EnsorcEnchantments.CURSE_FOOL.get())
        .ingredient(Items.BRICKS)
        .ingredient(Items.ROTTEN_FLESH, 8)
        .ingredient(Items.POISONOUS_POTATO, 4)
        .standardCost()
        .build());

    // Curse of Mercy
    output.accept(builder(EnsorcEnchantments.CURSE_MERCY.get())
        .ingredient(Items.STONE_SWORD)
        .ingredient(ItemTags.WOOL, 5)
        .ingredient(Items.CHAIN, 8)
        .standardCost()
        .build());

    // Thorns IV (the vanilla Thorns enchantment gets boosted a level)
    output.accept(ConditionalEnchantmentRecipe.builder()
        .id(new ResourceLocation(PrecisionEnchanter.MOD_ID, "ensorcellation/thorns_4"))
        .condition(ENSORC_LOADED)
        .result(Enchantments.THORNS, 4)
        .ingredient(Items.CACTUS, 64)
        .ingredient(Items.CACTUS, 64)
        .ingredient(Items.MOSS_BLOCK, 48)
        .ingredient(Items.END_CRYSTAL, 8)
        .cost(10)
        .build());

  }

}
