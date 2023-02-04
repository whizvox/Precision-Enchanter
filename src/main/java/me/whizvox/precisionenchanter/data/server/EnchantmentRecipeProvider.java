package me.whizvox.precisionenchanter.data.server;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EnchantmentRecipeProvider implements DataProvider {

  private final DataGenerator gen;
  private final String modId;

  public EnchantmentRecipeProvider(DataGenerator gen, String modId) {
    this.gen = gen;
    this.modId = modId;
  }

  @Override
  public CompletableFuture<?> run(CachedOutput out) {
    final PackOutput.PathProvider pathProvider = gen.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, "enchantment_recipes");
    List<CompletableFuture<?>> list = new ArrayList<>();
    Set<ResourceLocation> ids = new ObjectOpenHashSet<>();
    buildRecipes(recipe -> {
      if (!ids.add(recipe.getId())) {
        throw new IllegalArgumentException("Duplicate recipes: " + recipe.getId());
      }
      list.add(DataProvider.saveStable(out, EnchantmentRecipeManager.serialize(recipe), pathProvider.json(recipe.getId())));
    });
    return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
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
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .ingredient(Tags.Items.COBBLESTONE, 32)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 2)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .ingredient(Tags.Items.COBBLESTONE_DEEPSLATE, 32)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 3)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 4)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 32)
        .ingredient(Tags.Items.STORAGE_BLOCKS_NETHERITE)
        .build());
    output.accept(builder(Enchantments.SHARPNESS, 5)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .ingredient(Items.CRYING_OBSIDIAN, 16)
        .build());

    // Smite
    output.accept(builder(Enchantments.SMITE, 1)
        .ingredient(Items.ROTTEN_FLESH, 8)
        .ingredient(Tags.Items.BONES, 8)
        .build());
    output.accept(builder(Enchantments.SMITE, 2)
        .ingredient(Items.ROTTEN_FLESH, 16)
        .ingredient(Tags.Items.BONES, 16)
        .ingredient(Tags.Items.INGOTS_COPPER)
        .build());
    output.accept(builder(Enchantments.SMITE, 3)
        .ingredient(Items.ROTTEN_FLESH, 32)
        .ingredient(Tags.Items.BONES, 32)
        .ingredient(Tags.Items.INGOTS_IRON)
        .build());
    output.accept(builder(Enchantments.SMITE, 4)
        .ingredient(Items.ROTTEN_FLESH, 48)
        .ingredient(Tags.Items.BONES, 48)
        .ingredient(Tags.Items.INGOTS_GOLD)
        .build());
    output.accept(builder(Enchantments.SMITE, 5)
        .ingredient(Items.ROTTEN_FLESH, 64)
        .ingredient(Tags.Items.BONES, 64)
        .ingredient(Tags.Items.GEMS_DIAMOND)
        .build());

    // Bane of Arthropods
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 1)
        .ingredient(Items.SPIDER_EYE, 8)
        .ingredient(Tags.Items.STRING, 8)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 2)
        .ingredient(Items.SPIDER_EYE, 16)
        .ingredient(Tags.Items.STRING, 16)
        .ingredient(Tags.Items.INGOTS_COPPER)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 3)
        .ingredient(Items.SPIDER_EYE, 32)
        .ingredient(Tags.Items.STRING, 32)
        .ingredient(Tags.Items.INGOTS_IRON)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 4)
        .ingredient(Items.SPIDER_EYE, 48)
        .ingredient(Tags.Items.STRING, 48)
        .ingredient(Tags.Items.INGOTS_GOLD)
        .build());
    output.accept(builder(Enchantments.BANE_OF_ARTHROPODS, 5)
        .ingredient(Items.SPIDER_EYE, 64)
        .ingredient(Tags.Items.STRING, 64)
        .ingredient(Tags.Items.GEMS_DIAMOND)
        .build());

    // Knockback
    output.accept(builder(Enchantments.KNOCKBACK, 1)
        .ingredient(Items.WOODEN_SHOVEL)
        .ingredient(Tags.Items.SLIMEBALLS, 4)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 8)
        .build());
    output.accept(builder(Enchantments.KNOCKBACK, 2)
        .ingredient(Items.WOODEN_SHOVEL)
        .ingredient(Tags.Items.SLIMEBALLS, 16)
        .ingredient(Items.PISTON)
        .build());

    // Fire Aspect
    output.accept(builder(Enchantments.FIRE_ASPECT, 1)
        .ingredient(Items.FLINT_AND_STEEL)
        .ingredient(Items.MAGMA_BLOCK, 16)
        .build());
    output.accept(builder(Enchantments.FIRE_ASPECT, 2)
        .ingredient(Items.BLAZE_ROD, 6)
        .ingredient(Items.MAGMA_CREAM, 6)
        .build());

    // Looting
    output.accept(builder(Enchantments.MOB_LOOTING, 1)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 10)
        .ingredient(Items.RABBIT_FOOT)
        .build());
    output.accept(builder(Enchantments.MOB_LOOTING, 2)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 20)
        .ingredient(Tags.Items.GEMS_EMERALD)
        .build());
    output.accept(builder(Enchantments.MOB_LOOTING, 3)
        .ingredient(Items.GOLDEN_SWORD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 40)
        .ingredient(Items.HEART_OF_THE_SEA)
        .build());

    // Sweeping Edge
    output.accept(builder(Enchantments.SWEEPING_EDGE, 1)
        .ingredient(Items.WOODEN_SWORD)
        .ingredient(Items.MILK_BUCKET)
        .build());
    output.accept(builder(Enchantments.SWEEPING_EDGE, 2)
        .ingredient(Items.WOODEN_SWORD)
        .ingredient(Items.RABBIT_HIDE, 8)
        .build());
    output.accept(builder(Enchantments.SWEEPING_EDGE, 3)
        .ingredient(Items.WOODEN_SWORD)
        .ingredient(Items.SLIME_BLOCK, 32)
        .build());

    // Efficiency
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 1)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 4)
        .ingredient(Tags.Items.FEATHERS, 8)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 8)
        .ingredient(Items.ICE, 8)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 3)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 16)
        .ingredient(Items.PACKED_ICE, 8)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 32)
        .ingredient(Items.BLUE_ICE, 8)
        .build());
    output.accept(builder(Enchantments.BLOCK_EFFICIENCY, 5)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 64)
        .ingredient(Items.FIREWORK_ROCKET, 48)
        .build());

    // Silk Touch
    output.accept(builder(Enchantments.SILK_TOUCH)
        .ingredient(Items.COBWEB, 32)
        .ingredient(Tags.Items.GEMS_EMERALD, 8)
        .build());

    // Unbreaking
    output.accept(builder(Enchantments.UNBREAKING, 1)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.COBBLESTONE_DEEPSLATE, 64)
        .build());
    output.accept(builder(Enchantments.UNBREAKING, 2)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 16)
        .build());
    output.accept(builder(Enchantments.UNBREAKING, 3)
        .ingredient(Items.IRON_PICKAXE)
        .ingredient(Tags.Items.OBSIDIAN, 48)
        .build());

    // Fortune
    output.accept(builder(Enchantments.BLOCK_FORTUNE, 1)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 10)
        .ingredient(Items.RABBIT_FOOT)
        .build());
    output.accept(builder(Enchantments.BLOCK_FORTUNE, 2)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 20)
        .ingredient(Tags.Items.GEMS_EMERALD)
        .build());
    output.accept(builder(Enchantments.BLOCK_FORTUNE, 3)
        .ingredient(Items.GOLDEN_PICKAXE)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 40)
        .ingredient(Items.HEART_OF_THE_SEA)
        .build());

    // Power
    output.accept(builder(Enchantments.POWER_ARROWS, 1)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.GEMS_QUARTZ, 16)
        .ingredient(Tags.Items.COBBLESTONE, 32)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 2)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.GEMS_QUARTZ, 32)
        .ingredient(Tags.Items.COBBLESTONE_DEEPSLATE, 32)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 3)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.GEMS_QUARTZ, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 4)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 32)
        .ingredient(Tags.Items.STORAGE_BLOCKS_NETHERITE)
        .build());
    output.accept(builder(Enchantments.POWER_ARROWS, 5)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 64)
        .ingredient(Items.CRYING_OBSIDIAN, 16)
        .build());

    // Punch
    output.accept(builder(Enchantments.PUNCH_ARROWS, 1)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.SLIMEBALLS, 4)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 8)
        .build());
    output.accept(builder(Enchantments.PUNCH_ARROWS, 2)
        .ingredient(Items.DISPENSER)
        .ingredient(Tags.Items.SLIMEBALLS, 16)
        .ingredient(Items.PISTON)
        .build());

    // Flame
    output.accept(builder(Enchantments.FLAMING_ARROWS)
        .ingredient(Items.BLAZE_POWDER, 32)
        .ingredient(Tags.Items.NETHERRACK, 64)
        .build());

    // Infinity
    output.accept(builder(Enchantments.INFINITY_ARROWS)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .ingredient(Items.TOTEM_OF_UNDYING)
        .ingredient(Items.GHAST_TEAR)
        .build());

    // Luck of the Sea
    output.accept(builder(Enchantments.FISHING_LUCK, 1)
        .ingredient(Items.PUFFERFISH)
        .ingredient(Tags.Items.STORAGE_BLOCKS_GOLD)
        .build());
    output.accept(builder(Enchantments.FISHING_LUCK, 2)
        .ingredient(Items.PUFFERFISH, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_GOLD, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 6)
        .build());
    output.accept(builder(Enchantments.FISHING_LUCK, 3)
        .ingredient(Items.PUFFERFISH, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_GOLD, 16)
        .ingredient(Items.AXOLOTL_BUCKET)
        .build());

    // Lure
    output.accept(builder(Enchantments.FISHING_SPEED, 1)
        .ingredient(Items.GLOW_BERRIES, 8)
        .ingredient(Items.DIRT, 32)
        .build());
    output.accept(builder(Enchantments.FISHING_SPEED, 2)
        .ingredient(Items.GLOW_BERRIES, 16)
        .ingredient(Items.GLOW_INK_SAC)
        .build());
    output.accept(builder(Enchantments.FISHING_SPEED, 3)
        .ingredient(Items.GLOW_BERRIES, 32)
        .ingredient(Items.AXOLOTL_BUCKET)
        .build());

    // Loyalty
    output.accept(builder(Enchantments.LOYALTY, 1)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
        .ingredient(Items.SALMON)
        .build());
    output.accept(builder(Enchantments.LOYALTY, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 4)
        .ingredient(Tags.Items.BONES, 8)
        .ingredient(Items.BEEF, 8)
        .build());
    output.accept(builder(Enchantments.LOYALTY, 3)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 8)
        .ingredient(Items.AXOLOTL_BUCKET)
        .build());

    // Impaling
    output.accept(builder(Enchantments.IMPALING, 1)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 16)
        .ingredient(Tags.Items.COBBLESTONE, 32)
        .build());
    output.accept(builder(Enchantments.IMPALING, 2)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 32)
        .ingredient(Tags.Items.COBBLESTONE_DEEPSLATE, 32)
        .build());
    output.accept(builder(Enchantments.IMPALING, 3)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 64)
        .ingredient(Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .build());
    output.accept(builder(Enchantments.IMPALING, 4)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 32)
        .ingredient(Tags.Items.STORAGE_BLOCKS_NETHERITE)
        .build());
    output.accept(builder(Enchantments.IMPALING, 5)
        .ingredient(Items.IRON_SWORD)
        .ingredient(Tags.Items.GEMS_PRISMARINE, 64)
        .ingredient(Items.CRYING_OBSIDIAN, 16)
        .build());

    // Riptide
    output.accept(builder(Enchantments.RIPTIDE, 1)
        .ingredient(Items.ELYTRA)
        .ingredient(Items.PRISMARINE, 8)
        .build());
    output.accept(builder(Enchantments.RIPTIDE, 2)
        .ingredient(Items.ELYTRA)
        .ingredient(Items.NAUTILUS_SHELL, 8)
        .build());
    output.accept(builder(Enchantments.RIPTIDE, 3)
        .ingredient(Items.ELYTRA)
        .ingredient(Items.HEART_OF_THE_SEA)
        .build());

    // Channeling
    output.accept(builder(Enchantments.CHANNELING)
        .ingredient(Items.LIGHTNING_ROD, 8)
        .ingredient(Tags.Items.DUSTS_GLOWSTONE, 16)
        .ingredient(Items.IRON_AXE)
        .build());

    // Multishot
    output.accept(builder(Enchantments.MULTISHOT)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .ingredient(Items.SPECTRAL_ARROW, 16)
        .build());

    // Quick Charge
    output.accept(builder(Enchantments.QUICK_CHARGE, 1)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 16)
        .ingredient(Tags.Items.STRING, 16)
        .build());
    output.accept(builder(Enchantments.QUICK_CHARGE, 2)
        .ingredient(Tags.Items.DUSTS_REDSTONE, 48)
        .ingredient(Tags.Items.STRING, 32)
        .ingredient(Tags.Items.FEATHERS, 8)
        .build());
    output.accept(builder(Enchantments.QUICK_CHARGE, 3)
        .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 16)
        .ingredient(Tags.Items.STRING, 48)
        .ingredient(Items.CLAY, 8)
        .build());

    // Piercing
    output.accept(builder(Enchantments.PIERCING, 1)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE)
        .ingredient(Items.COBBLESTONE, 8)
        .build());
    output.accept(builder(Enchantments.PIERCING, 2)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE, 3)
        .ingredient(Items.FLINT, 8)
        .build());
    output.accept(builder(Enchantments.PIERCING, 3)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE, 9)
        .ingredient(Items.OBSIDIAN, 8)
        .build());
    output.accept(builder(Enchantments.PIERCING, 4)
        .ingredient(Items.GRINDSTONE)
        .ingredient(Items.PHANTOM_MEMBRANE, 27)
        .ingredient(Items.CRYING_OBSIDIAN, 8)
        .build());

    // Mending
    output.accept(builder(Enchantments.MENDING)
        .ingredient(Items.MOSS_BLOCK, 64)
        .ingredient(Items.RESPAWN_ANCHOR)
        .ingredient(Items.SCULK_CATALYST)
        .build());

    // Curse of Vanishing
    output.accept(builder(Enchantments.VANISHING_CURSE)
        .ingredient(Items.POISONOUS_POTATO, 4)
        .ingredient(Items.SUSPICIOUS_STEW)
        .ingredient(Items.PHANTOM_MEMBRANE, 4)
        .build());

    // Protection
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 1)
        .ingredient(Items.SHIELD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER)
        .build());
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 2)
        .ingredient(Items.SHIELD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Items.DEEPSLATE, 64)
        .build());
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 3)
        .ingredient(Items.SHIELD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Items.OBSIDIAN, 16)
        .build());
    output.accept(builder(Enchantments.ALL_DAMAGE_PROTECTION, 4)
        .ingredient(Items.SHIELD)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 24)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COAL, 12)
        .build());

    // Fire Protection
    output.accept(builder(Enchantments.FIRE_PROTECTION, 1)
        .ingredient(Items.MAGMA_CREAM, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER)
        .build());
    output.accept(builder(Enchantments.FIRE_PROTECTION, 2)
        .ingredient(Items.MAGMA_CREAM, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Items.DEEPSLATE, 64)
        .build());
    output.accept(builder(Enchantments.FIRE_PROTECTION, 3)
        .ingredient(Items.MAGMA_CREAM, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Items.OBSIDIAN, 16)
        .build());
    output.accept(builder(Enchantments.FIRE_PROTECTION, 4)
        .ingredient(Items.MAGMA_CREAM, 16)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 24)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COAL, 12)
        .build());

    // Feather Falling
    output.accept(builder(Enchantments.FALL_PROTECTION, 1)
        .ingredient(ItemTags.WOOL, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER)
        .build());
    output.accept(builder(Enchantments.FALL_PROTECTION, 2)
        .ingredient(ItemTags.WOOL, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Items.DEEPSLATE, 64)
        .build());
    output.accept(builder(Enchantments.FALL_PROTECTION, 3)
        .ingredient(ItemTags.WOOL, 16)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Tags.Items.OBSIDIAN, 16)
        .build());
    output.accept(builder(Enchantments.FALL_PROTECTION, 4)
        .ingredient(ItemTags.WOOL, 32)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 24)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COAL, 12)
        .build());

    // Blast Protection
    output.accept(builder(Enchantments.BLAST_PROTECTION, 1)
        .ingredient(Items.TNT, 2)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER)
        .build());
    output.accept(builder(Enchantments.BLAST_PROTECTION, 2)
        .ingredient(Items.TNT, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Items.DEEPSLATE, 64)
        .build());
    output.accept(builder(Enchantments.BLAST_PROTECTION, 3)
        .ingredient(Items.TNT, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Tags.Items.OBSIDIAN, 16)
        .build());
    output.accept(builder(Enchantments.BLAST_PROTECTION, 4)
        .ingredient(Items.TNT, 16)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 24)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COAL, 12)
        .build());

    // Projectile Protection
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 1)
        .ingredient(Items.ARROW, 4)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER)
        .build());
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 2)
        .ingredient(Items.ARROW, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 2)
        .ingredient(Items.DEEPSLATE, 64)
        .build());
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 3)
        .ingredient(Items.ARROW, 16)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 4)
        .ingredient(Tags.Items.OBSIDIAN, 16)
        .build());
    output.accept(builder(Enchantments.PROJECTILE_PROTECTION, 4)
        .ingredient(Items.ARROW, 32)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COPPER, 8)
        .ingredient(Tags.Items.STORAGE_BLOCKS_IRON, 24)
        .ingredient(Tags.Items.STORAGE_BLOCKS_COAL, 12)
        .build());

    // Respiration
    output.accept(builder(Enchantments.RESPIRATION, 1)
        .ingredient(Items.TURTLE_HELMET)
        .ingredient(Items.NETHER_WART, 4)
        .build());
    output.accept(builder(Enchantments.RESPIRATION, 2)
        .ingredient(Items.TURTLE_HELMET)
        .ingredient(Items.NETHER_WART_BLOCK)
        .build());
    output.accept(builder(Enchantments.RESPIRATION, 3)
        .ingredient(Items.TURTLE_HELMET)
        .ingredient(Items.NETHER_WART_BLOCK, 2)
        .ingredient(Items.SEAGRASS, 16)
        .build());

    // Aqua Affinity
    output.accept(builder(Enchantments.AQUA_AFFINITY)
        .ingredient(Items.TURTLE_EGG, 8)
        .ingredient(Items.SEA_PICKLE, 8)
        .build());

    // Thorns
    output.accept(builder(Enchantments.THORNS, 1)
        .ingredient(Items.CACTUS, 16)
        .ingredient(Items.MOSS_BLOCK, 6)
        .ingredient(Items.BONE_MEAL, 6)
        .build());
    output.accept(builder(Enchantments.THORNS, 2)
        .ingredient(Items.CACTUS, 32)
        .ingredient(Items.MOSS_BLOCK, 12)
        .ingredient(Items.GHAST_TEAR, 3)
        .build());
    output.accept(builder(Enchantments.THORNS, 3)
        .ingredient(Items.CACTUS, 64)
        .ingredient(Items.MOSS_BLOCK, 24)
        .ingredient(Items.END_CRYSTAL, 4)
        .build());

    // Depth Strider
    output.accept(builder(Enchantments.DEPTH_STRIDER, 1)
        .ingredient(Items.MAGMA_BLOCK, 6)
        .ingredient(ItemTags.FISHES, 4)
        .build());
    output.accept(builder(Enchantments.DEPTH_STRIDER, 2)
        .ingredient(Items.MAGMA_BLOCK, 12)
        .ingredient(Items.TURTLE_EGG, 8)
        .build());
    output.accept(builder(Enchantments.DEPTH_STRIDER, 3)
        .ingredient(Items.MAGMA_BLOCK, 24)
        .ingredient(Items.TRIDENT)
        .build());

    // Frost Walker
    output.accept(builder(Enchantments.FROST_WALKER, 1)
        .ingredient(Items.PACKED_ICE, 8)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .build());
    output.accept(builder(Enchantments.FROST_WALKER, 2)
        .ingredient(Items.BLUE_ICE, 8)
        .ingredient(Tags.Items.CHESTS_ENDER)
        .build());

    // Curse of Binding
    output.accept(builder(Enchantments.BINDING_CURSE)
        .ingredient(Items.SLIME_BALL, 8)
        .ingredient(Items.COBWEB, 4)
        .ingredient(Items.POISONOUS_POTATO)
        .build());

    // Soul Speed
    output.accept(builder(Enchantments.SOUL_SPEED, 1)
        .ingredient(Tags.Items.FEATHERS, 8)
        .ingredient(Items.SOUL_SAND, 8)
        .ingredient(Items.SOUL_SOIL, 8)
        .build());
    output.accept(builder(Enchantments.SOUL_SPEED, 2)
        .ingredient(Tags.Items.FEATHERS, 16)
        .ingredient(Items.SOUL_SAND, 16)
        .ingredient(Items.SOUL_SOIL, 16)
        .ingredient(Items.PACKED_ICE, 4)
        .build());
    output.accept(builder(Enchantments.SOUL_SPEED, 3)
        .ingredient(Tags.Items.FEATHERS, 16)
        .ingredient(Items.SOUL_SAND, 16)
        .ingredient(Items.SOUL_SOIL, 16)
        .ingredient(Items.BLUE_ICE, 4)
        .build());

    // Swift Sneak
    output.accept(builder(Enchantments.SWIFT_SNEAK, 1)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 8)
        .build());
    output.accept(builder(Enchantments.SWIFT_SNEAK, 2)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 16)
        .ingredient(Items.PACKED_ICE, 4)
        .build());
    output.accept(builder(Enchantments.SWIFT_SNEAK, 3)
        .ingredient(Items.LEATHER_BOOTS)
        .ingredient(Tags.Items.FEATHERS, 16)
        .ingredient(Items.BLUE_ICE, 4)
        .build());

  }

}
