package me.whizvox.precisionenchanter.data.client;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import me.whizvox.precisionenchanter.common.registry.PEItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class PELanguageProvider extends LanguageProvider {

  public PELanguageProvider(PackOutput output, String locale) {
    super(output, PrecisionEnchanter.MOD_ID, locale);
  }

  @Override
  protected void addTranslations() {
    addBlock(PEBlocks.ENCHANTERS_WORKBENCH, "Enchanter's Workbench");
    addBlock(PEBlocks.PRECISION_GRINDSTONE, "Precision Grindstone");
    addItem(PEItems.QUILL, "Quill");
    addItem(PEItems.ENCHANTED_QUILL, "Enchanted Quill");
    add(PELang.Keys.CREATIVE_MODE_TAB, PrecisionEnchanter.MOD_NAME);
    add(PELang.Keys.CONTAINER_WORKBENCH_TITLE, "Enchanter's Workbench");
    add(PELang.Keys.CONTAINER_GRINDSTONE_TITLE, "Precision Grindstone");
    add(PELang.Keys.SCREEN_SELECT_NEXT, "Select next");
    add(PELang.Keys.SCREEN_SELECT_PREV, "Select previous");
    add(PELang.Keys.SCREEN_LOADING, "Loading");
    add(PELang.Keys.WORKBENCH_SHOW_RECIPES, "Show recipes");
    add(PELang.Keys.WORKBENCH_HIDE_RECIPES, "Hide recipes");
    add(PELang.Keys.TABLET_SEARCH_HINT, "Search");
    add(PELang.Keys.TABLET_SYNC_FAILED, "Failed to sync recipes");
    add(PELang.Keys.TABLET_SHOW_ALL, "Showing all");
    add(PELang.Keys.TABLET_SHOW_CRAFTABLE, "Showing craftable");
    add(PELang.Keys.N_MORE, "... +%s more");
    add(PELang.Keys.NO_IMPOSSIBLE_RECIPES, "No impossible recipes found");
    add(PELang.Keys.FOUND_IMPOSSIBLE_RECIPES, "%s impossible recipe(s) found:");
    add(PELang.Keys.NO_FREE_RECIPES, "No free recipes found");
    add(PELang.Keys.FOUND_FREE_RECIPES, "%s free recipe(s) found:");
  }

}
