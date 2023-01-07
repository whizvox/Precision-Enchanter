package me.whizvox.precisionenchanter.data.server;

import me.whizvox.precisionenchanter.common.registry.PEItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static me.whizvox.precisionenchanter.common.PrecisionEnchanter.modLoc;

public class PERecipeProvider extends RecipeProvider {

  public PERecipeProvider(PackOutput output) {
    super(output);
  }

  private static CriterionTriggerInstance hasItems(ItemLike... items) {
    return InventoryChangeTrigger.TriggerInstance.hasItems(items);
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> writer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, PEItems.QUILL.get())
        .pattern("F")
        .pattern("I")
        .pattern("G")
        .define('F', Tags.Items.FEATHERS)
        .define('I', Items.INK_SAC)
        .define('G', Tags.Items.GLASS_PANES)
        .unlockedBy("has_ink_sac", hasItems(Items.INK_SAC))
        .save(writer);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, PEItems.QUILL.get())
        .requires(PEItems.QUILL.get())
        .requires(Items.INK_SAC)
        .unlockedBy("has_quill", hasItems(PEItems.QUILL.get()))
        .save(writer, modLoc("quill_repair_1"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, PEItems.QUILL.get())
        .requires(PEItems.QUILL.get())
        .requires(Tags.Items.DYES_BLACK)
        .unlockedBy("has_quill", hasItems(PEItems.QUILL.get()))
        .save(writer, modLoc("quill_repair_2"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, PEItems.ENCHANTED_QUILL.get())
        .requires(PEItems.QUILL.get())
        .requires(Items.DRAGON_BREATH)
        .requires(Tags.Items.DUSTS_GLOWSTONE)
        .unlockedBy("has_dragon_breath", hasItems(Items.DRAGON_BREATH))
        .save(writer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PEItems.ENCHANTERS_WORKBENCH.get())
        .pattern(" Q ")
        .pattern("RRR")
        .pattern("DSD")
        .define('Q', PEItems.ENCHANTED_QUILL.get())
        .define('R', Items.RED_CARPET)
        .define('D', Items.DEEPSLATE)
        .define('S', Items.SMOOTH_STONE_SLAB)
        .unlockedBy("has_enchanted_quill", hasItems(PEItems.ENCHANTED_QUILL.get()))
        .save(writer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PEItems.PRECISION_GRINDSTONE.get())
        .pattern(" Q ")
        .pattern("LDL")
        .pattern("WWW")
        .define('Q', PEItems.ENCHANTED_QUILL.get())
        .define('L', Tags.Items.GEMS_LAPIS)
        .define('D', Items.DIORITE_SLAB)
        .define('W', ItemTags.WOODEN_SLABS)
        .unlockedBy("has_enchanted_quill", hasItems(PEItems.ENCHANTED_QUILL.get()))
        .save(writer);
  }

}
