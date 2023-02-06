package me.whizvox.precisionenchanter.data.server;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;

public class EnchantmentRecipeProvider implements DataProvider {

  private final DataGenerator gen;
  private final String modId;

  public EnchantmentRecipeProvider(DataGenerator gen, String modId) {
    this.gen = gen;
    this.modId = modId;
  }

  @Override
  public void run(CachedOutput out) {
    DataGenerator.PathProvider pathProvider = gen.createPathProvider(DataGenerator.Target.DATA_PACK, "enchantment_recipes");
    Set<ResourceLocation> ids = new ObjectOpenHashSet<>();
    buildRecipes(recipe -> {
      if (!ids.add(recipe.getId())) {
        throw new IllegalArgumentException("Duplicate recipes: " + recipe.getId());
      }
      try {
        DataProvider.saveStable(out, EnchantmentRecipeManager.serialize(recipe), pathProvider.json(recipe.getId()));
      } catch (IOException e) {
        PELog.LOGGER.error(PELog.M_DATAGEN, "Could not save enchantment recipe: {}", recipe.getId());
      }
    });
  }

  @Override
  public String getName() {
    return "EnchantmentRecipes";
  }

  public EnchantmentRecipe.Builder builder(Enchantment result, int level) {
    return EnchantmentRecipe.builder()
        .id(new ResourceLocation(modId, ForgeRegistries.ENCHANTMENTS.getKey(result).getPath() + "_" + level))
        .result(result, level);
  }

  public EnchantmentRecipe.Builder builder(Enchantment result) {
    return EnchantmentRecipe.builder()
        .id(new ResourceLocation(modId, ForgeRegistries.ENCHANTMENTS.getKey(result).getPath()))
        .result(result, 1);
  }

  public void buildRecipes(Consumer<EnchantmentRecipe> output) {

    // Sharpness
    output.accept(builder(Enchantments.SHARPNESS, 1)
        .ingredient(Items.SMITHING_TABLE)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .ingredient(Tags.Items.COBBLESTONE, 32)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 2)
        .ingredient(Items.SMITHING_TABLE)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .ingredient(Tags.Items.COBBLESTONE_DEEPSLATE, 32)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 3)
        .ingredient(Items.SMITHING_TABLE)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .ingredient(Tags.Items.OBSIDIAN, 8)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 4)
        .ingredient(Items.SMITHING_TABLE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 32)
        .ingredient(Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .cost(5)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 5)
        .ingredient(Items.SMITHING_TABLE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_NETHERITE)
        .cost(7)
        .build());

    // Smite
    output.accept(builder(Enchantments.SMITE, 1)
        .ingredient(Items.ROTTEN_FLESH, 8)
        .ingredient(Tags.Items.BONES, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.SMITE, 2)
        .ingredient(Items.ROTTEN_FLESH, 16)
        .ingredient(Tags.Items.BONES, 16)
        .ingredient(Tags.Items.INGOTS_COPPER)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.SMITE, 3)
        .ingredient(Items.ROTTEN_FLESH, 32)
        .ingredient(Tags.Items.BONES, 32)
        .ingredient(Tags.Items.INGOTS_IRON)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.SMITE, 4)
        .ingredient(Items.ROTTEN_FLESH, 48)
        .ingredient(Tags.Items.BONES, 48)
        .ingredient(Tags.Items.INGOTS_GOLD)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.SMITE, 5)
        .ingredient(Items.ROTTEN_FLESH, 64)
        .ingredient(Tags.Items.BONES, 64)
        .ingredient(Tags.Items.GEMS_DIAMOND)
        .cost(5)
        .build());

    // Bane of Arthropods
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 1)
        .ingredient(Items.SPIDER_EYE, 8)
        .ingredient(Tags.Items.STRING, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 2)
        .ingredient(Items.SPIDER_EYE, 16)
        .ingredient(Tags.Items.STRING, 16)
        .ingredient(Tags.Items.INGOTS_COPPER)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 3)
        .ingredient(Items.SPIDER_EYE, 32)
        .ingredient(Tags.Items.STRING, 32)
        .ingredient(Tags.Items.INGOTS_IRON)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 4)
        .ingredient(Items.SPIDER_EYE, 48)
        .ingredient(Tags.Items.STRING, 48)
        .ingredient(Tags.Items.INGOTS_GOLD)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 5)
        .ingredient(Items.SPIDER_EYE, 64)
        .ingredient(Tags.Items.STRING, 64)
        .ingredient(Tags.Items.GEMS_DIAMOND)
        .cost(5)
        .build());

    // Knockback
    output.accept(builder(Enchantments.KNOCKBACK, 1)
        .ingredient(Items.WOODEN_SHOVEL)
        .ingredient(Items.SLIME_BLOCK, 2)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.KNOCKBACK, 2)
        .ingredient(Items.WOODEN_SHOVEL)
        .ingredient(Items.SLIME_BLOCK, 4)
        .ingredient(Items.PISTON)
        .cost(3)
        .build());

    // Fire Aspect
    output.accept(builder(Enchantments.FIRE_ASPECT, 1)
        .ingredient(Items.FIRE_CHARGE, 3)
        .ingredient(Items.BLAZE_POWDER, 16)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.FIRE_ASPECT, 2)
        .ingredient(Items.FIRE_CHARGE, 6)
        .ingredient(Items.BLAZE_POWDER, 32)
        .ingredient(Items.LAVA_BUCKET)
        .ingredient(Items.MAGMA_BLOCK, 16)
        .standardCost()
        .build());

    // Looting
    output.accept(builder(Enchantments.MOB_LOOTING, 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 10)
        .ingredient(Items.RABBIT_FOOT)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.MOB_LOOTING, 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 20)
        .ingredient(Tags.Items.GEMS_EMERALD)
        .cost(6)
        .build());
    output.accept(builder(Enchantments.MOB_LOOTING, 3)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 40)
        .ingredient(Items.HEART_OF_THE_SEA)
        .cost(10)
        .build());

    // Sweeping Edge
    output.accept(builder(Enchantments.SWEEPING_EDGE, 1)
        .ingredient(Items.WOODEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 4)
        .ingredient(Tags.Items.COBBLESTONE, 16)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.SWEEPING_EDGE, 2)
        .ingredient(Items.WOODEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 8)
        .ingredient(Tags.Items.COBBLESTONE_DEEPSLATE, 16)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.SWEEPING_EDGE, 3)
        .ingredient(Items.WOODEN_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 8)
        .ingredient(Items.OBSIDIAN, 8)
        .cost(7)
        .build());

    // Efficiency
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 1)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 4)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 2)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 8)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 3)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 16)
        .cost(5)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 4)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 32)
        .cost(6)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 5)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 64)
        .cost(8)
        .build());

    // Silk Touch
    output.accept(builder(Enchantments.SILK_TOUCH)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Items.STRING, 32)
        .ingredient(Tags.Items.GEMS_EMERALD, 6)
        .cost(5)
        .build());

    // Unbreaking
    output.accept(builder(Enchantments.UNBREAKING, 1)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.COBBLESTONE_DEEPSLATE, 64)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.UNBREAKING, 2)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 16)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.UNBREAKING, 3)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.OBSIDIAN, 48)
        .cost(7)
        .build());

    // Fortune
    output.accept(builder(Enchantments.BLOCK_FORTUNE, 1)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 10)
        .ingredient(Items.RABBIT_FOOT)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.BLOCK_FORTUNE, 2)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 20)
        .ingredient(Tags.Items.GEMS_EMERALD)
        .cost(6)
        .build());
    output.accept(builder(Enchantments.BLOCK_FORTUNE, 3)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 40)
        .ingredient(Items.HEART_OF_THE_SEA)
        .cost(10)
        .build());

    // Power
    output.accept(builder(Enchantments.POWER_ARROWS, 1)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .ingredient(Tags.Items.COBBLESTONE, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 2)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .ingredient(Items.FLINT, 8)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 3)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .ingredient(Tags.Items.OBSIDIAN, 8)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 4)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 32)
        .ingredient(Tags.Items.OBSIDIAN, 16)
        .cost(6)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 5)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .cost(7)
        .build());

    // Punch
    output.accept(builder(Enchantments.PUNCH_ARROWS, 1)
        .ingredient(Items.DISPENSER)
        .ingredient(Items.SLIME_BLOCK, 2)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.PUNCH_ARROWS, 2)
        .ingredient(Items.DISPENSER)
        .ingredient(Items.SLIME_BLOCK, 4)
        .ingredient(Items.PISTON)
        .cost(3)
        .build());

    // Flame
    output.accept(builder(Enchantments.FLAMING_ARROWS)
        .ingredient(Items.BOW)
        .ingredient(Items.FIRE_CHARGE, 16)
        .ingredient(Tags.Items.NETHERRACK, 64)
        .cost(5)
        .build());

    // Infinity
    output.accept(builder(Enchantments.INFINITY_ARROWS)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .ingredient(Items.TOTEM_OF_UNDYING)
        .ingredient(Items.GHAST_TEAR)
        .cost(9)
        .build());

    // Luck of the Sea
    output.accept(builder(Enchantments.FISHING_LUCK, 1)
        .ingredient(Items.FISHING_ROD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 7)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.FISHING_LUCK, 2)
        .ingredient(Items.FISHING_ROD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 14)
        .ingredient(Items.RABBIT_FOOT)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.FISHING_LUCK, 3)
        .ingredient(Items.FISHING_ROD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 28)
        .ingredient(Tags.Items.GEMS_EMERALD)
        .standardCost()
        .build());

    // Lure
    output.accept(builder(Enchantments.FISHING_SPEED, 1)
        .ingredient(Items.FISHING_ROD)
        .ingredient(Items.GLOW_BERRIES, 8)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.FISHING_SPEED, 2)
        .ingredient(Items.FISHING_ROD)
        .ingredient(Items.GLOW_BERRIES, 16)
        .ingredient(Items.GLOW_INK_SAC, 4)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.FISHING_SPEED, 3)
        .ingredient(Items.FISHING_ROD)
        .ingredient(Items.GLOW_BERRIES, 32)
        .ingredient(Items.GLOW_INK_SAC, 8)
        .cost(6)
        .build());

    // Loyalty
    output.accept(builder(Enchantments.LOYALTY, 1)
        .ingredient(Items.AXOLOTL_BUCKET)
        .ingredient(Items.ENDER_EYE, 4)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.LOYALTY, 2)
        .ingredient(Items.AXOLOTL_BUCKET)
        .ingredient(Items.ENDER_EYE, 8)
        .ingredient(Items.SHULKER_SHELL, 2)
        .cost(6)
        .build());
    output.accept(builder(Enchantments.LOYALTY, 3)
        .ingredient(Items.AXOLOTL_BUCKET)
        .ingredient(Items.ENDER_EYE, 16)
        .ingredient(Items.SHULKER_SHELL, 4)
        .ingredient(Items.END_ROD)
        .cost(8)
        .build());

    // Impaling
    output.accept(builder(Enchantments.IMPALING, 1)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 3)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.IMPALING, 2)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 6)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.IMPALING, 3)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 12)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.IMPALING, 4)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 24)
        .cost(5)
        .build());
    output.accept(builder(Enchantments.IMPALING, 5)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 48)
        .cost(6)
        .build());

    // Riptide
    output.accept(builder(Enchantments.RIPTIDE, 1)
        .ingredient(Items.ELYTRA)
        .ingredient(Items.PRISMARINE, 8)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.RIPTIDE, 2)
        .ingredient(Items.ELYTRA)
        .ingredient(Items.NAUTILUS_SHELL, 8)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.RIPTIDE, 3)
        .ingredient(Items.ELYTRA)
        .ingredient(Items.HEART_OF_THE_SEA)
        .standardCost()
        .build());

    // Channeling
    output.accept(builder(Enchantments.CHANNELING)
        .ingredient(Items.LIGHTNING_ROD)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 16)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 16)
        .ingredient(Tags.Items.INGOTS_IRON, 4)
        .cost(6)
        .build());

    // Multishot
    output.accept(builder(Enchantments.MULTISHOT)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .ingredient(Items.SPECTRAL_ARROW, 16)
        .cost(6)
        .build());

    // Quick Charge
    output.accept(builder(Enchantments.QUICK_CHARGE, 1)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 32)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.QUICK_CHARGE, 2)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 64)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.QUICK_CHARGE, 3)
        .ingredient(Items.FLETCHING_TABLE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 14)
        .cost(4)
        .build());

    // Piercing
    output.accept(builder(Enchantments.PIERCING, 1)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE)
        .ingredient(Items.COBBLESTONE, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.PIERCING, 2)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE, 3)
        .ingredient(Items.FLINT, 8)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.PIERCING, 3)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE, 9)
        .ingredient(Tags.Items.INGOTS_COPPER, 8)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.PIERCING, 4)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE, 27)
        .ingredient(Tags.Items.OBSIDIAN, 8)
        .cost(6)
        .build());

    // Mending
    output.accept(builder(Enchantments.MENDING)
        .ingredient(Items.MOSS_BLOCK, 64)
        .ingredient(Items.RESPAWN_ANCHOR)
        .ingredient(Items.SCULK_CATALYST)
        .cost(10)
        .build());

    // Curse of Vanishing
    output.accept(builder(Enchantments.VANISHING_CURSE)
        .ingredient(Items.POISONOUS_POTATO, 4)
        .ingredient(Items.SUSPICIOUS_STEW)
        .ingredient(Items.PHANTOM_MEMBRANE, 4)
        .cost(3)
        .build());

    // Protection
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 1)
        .ingredient(Items.SHIELD)
        .ingredient(ItemTags.WOOL, 4)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 2)
        .ingredient(Items.SHIELD)
        .ingredient(ItemTags.WOOL, 8)
        .ingredient(Items.IRON_AXE)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 3)
        .ingredient(Items.SHIELD)
        .ingredient(ItemTags.WOOL, 16)
        .ingredient(Items.DRAGON_BREATH)
        .cost(7)
        .build());
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 4)
        .ingredient(Items.SHIELD)
        .ingredient(ItemTags.WOOL, 32)
        .ingredient(Items.NETHER_STAR)
        .cost(9)
        .build());

    // Fire Protection
    output.accept(builder(Enchantments.FIRE_PROTECTION, 1)
        .ingredient(Items.MAGMA_CREAM, 2)
        .ingredient(ItemTags.WOOL, 4)
        .ingredient(Items.SNOW_BLOCK, 4)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.FIRE_PROTECTION, 2)
        .ingredient(Items.MAGMA_CREAM, 4)
        .ingredient(ItemTags.WOOL, 8)
        .ingredient(Items.SNOW_BLOCK, 8)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.FIRE_PROTECTION, 3)
        .ingredient(Items.MAGMA_CREAM, 8)
        .ingredient(ItemTags.WOOL, 16)
        .ingredient(Items.POWDER_SNOW_BUCKET)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.FIRE_PROTECTION, 4)
        .ingredient(Items.MAGMA_CREAM, 16)
        .ingredient(ItemTags.WOOL, 32)
        .ingredient(Items.POWDER_SNOW_BUCKET)
        .ingredient(Items.DRAGON_BREATH)
        .cost(6)
        .build());

    // Feather Falling
    output.accept(builder(Enchantments.FALL_PROTECTION, 1)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(ItemTags.WOOL, 4)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.FALL_PROTECTION, 2)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(ItemTags.WOOL, 8)
        .ingredient(Items.SLIME_BLOCK, 4)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.FALL_PROTECTION, 3)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(ItemTags.WOOL, 16)
        .ingredient(Items.SLIME_BLOCK, 8)
        .ingredient(Items.SPONGE, 4)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.FALL_PROTECTION, 4)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(ItemTags.WOOL, 32)
        .ingredient(Items.SLIME_BLOCK, 16)
        .ingredient(Items.SPONGE, 8)
        .standardCost()
        .build());

    // Blast Protection
    output.accept(builder(Enchantments.BLAST_PROTECTION, 1)
        .ingredient(Items.TNT, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.BLAST_PROTECTION, 2)
        .ingredient(Items.TNT, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Tags.Items.COBBLESTONE, 32)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.BLAST_PROTECTION, 3)
        .ingredient(Items.TNT, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Items.COBBLED_DEEPSLATE, 32)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.BLAST_PROTECTION, 4)
        .ingredient(Items.TNT, 16)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.OBSIDIAN, 24)
        .cost(6)
        .build());

    // Projectile Protection
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 1)
        .ingredient(Items.ARROW)
        .ingredient(ItemTags.WOOL, 4)
        .ingredient(Items.CHAIN, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 2)
        .ingredient(Items.ARROW)
        .ingredient(ItemTags.WOOL, 8)
        .ingredient(Items.CHAIN, 16)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 3)
        .ingredient(Items.ARROW)
        .ingredient(ItemTags.WOOL, 16)
        .ingredient(Items.SHIELD)
        .ingredient(Items.PHANTOM_MEMBRANE, 8)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 4)
        .ingredient(Items.ARROW)
        .ingredient(ItemTags.WOOL, 16)
        .ingredient(Items.DRAGON_BREATH)
        .cost(5)
        .build());

    // Respiration
    output.accept(builder(Enchantments.RESPIRATION, 1)
        .ingredient(Items.TURTLE_HELMET)
        .ingredient(Items.NETHER_WART, 4)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.RESPIRATION, 2)
        .ingredient(Items.TURTLE_HELMET)
        .ingredient(Items.NETHER_WART_BLOCK)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.RESPIRATION, 3)
        .ingredient(Items.TURTLE_HELMET)
        .ingredient(Items.NETHER_WART_BLOCK, 2)
        .ingredient(Items.SEAGRASS, 16)
        .cost(6)
        .build());

    // Aqua Affinity
    output.accept(builder(Enchantments.AQUA_AFFINITY)
        .ingredient(Items.TURTLE_HELMET)
        .ingredient(Items.DIAMOND_PICKAXE)
        .ingredient(Items.SEA_PICKLE, 8)
        .cost(4)
        .build());

    // Thorns
    output.accept(builder(Enchantments.THORNS, 1)
        .ingredient(Items.CACTUS, 16)
        .ingredient(Items.MOSS_BLOCK, 6)
        .ingredient(Items.BONE_MEAL, 6)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.THORNS, 2)
        .ingredient(Items.CACTUS, 32)
        .ingredient(Items.MOSS_BLOCK, 12)
        .ingredient(Items.GHAST_TEAR, 3)
        .cost(5)
        .build());
    output.accept(builder(Enchantments.THORNS, 3)
        .ingredient(Items.CACTUS, 64)
        .ingredient(Items.MOSS_BLOCK, 24)
        .ingredient(Items.END_CRYSTAL, 4)
        .cost(7)
        .build());

    // Depth Strider
    output.accept(builder(Enchantments.DEPTH_STRIDER, 1)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Items.SUGAR, 8)
        .ingredient(ItemTags.FISHES, 8)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.DEPTH_STRIDER, 2)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Items.SUGAR, 8)
        .ingredient(Items.TURTLE_EGG, 8)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.DEPTH_STRIDER, 3)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Items.SUGAR, 8)
        .ingredient(Items.TRIDENT)
        .cost(6)
        .build());

    // Frost Walker
    output.accept(builder(Enchantments.FROST_WALKER, 1)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Items.PACKED_ICE, 8)
        .standardCost()
        .build());
    output.accept(builder(Enchantments.FROST_WALKER, 2)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Items.BLUE_ICE, 8)
        .standardCost()
        .build());

    // Curse of Binding
    output.accept(builder(Enchantments.BINDING_CURSE)
        .ingredient(Items.HONEY_BLOCK, 8)
        .ingredient(Items.COBWEB, 4)
        .ingredient(Items.POISONOUS_POTATO)
        .ingredient(Items.CHAIN, 4)
        .standardCost()
        .build());

    // Soul Speed
    output.accept(builder(Enchantments.SOUL_SPEED, 1)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Items.SOUL_SAND, 8)
        .ingredient(Items.SOUL_SOIL, 8)
        .cost(3)
        .build());
    output.accept(builder(Enchantments.SOUL_SPEED, 2)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Items.SOUL_SAND, 16)
        .ingredient(Items.SOUL_SOIL, 16)
        .ingredient(Items.PACKED_ICE, 4)
        .cost(4)
        .build());
    output.accept(builder(Enchantments.SOUL_SPEED, 3)
        .ingredient(Items.GOLDEN_BOOTS)
        .ingredient(Items.SOUL_SAND, 32)
        .ingredient(Items.SOUL_SOIL, 32)
        .ingredient(Items.BLUE_ICE, 2)
        .cost(6)
        .build());

    // Swift Sneak
    output.accept(builder(Enchantments.SWIFT_SNEAK, 1)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 8)
        .cost(1)
        .build());
    output.accept(builder(Enchantments.SWIFT_SNEAK, 2)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 16)
        .ingredient(Items.PACKED_ICE, 4)
        .cost(2)
        .build());
    output.accept(builder(Enchantments.SWIFT_SNEAK, 3)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 32)
        .ingredient(Items.BLUE_ICE, 2)
        .cost(4)
        .build());

  }

}
