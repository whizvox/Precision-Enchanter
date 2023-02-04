package me.whizvox.precisionenchanter.data.client;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
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
    add(PELang.Keys.CREATIVE_MODE_TAB, "Precision Enchanting");
    add(PELang.Keys.CONTAINER_ENCHANTERS_WORKBENCH, "Enchanter's Workbench");
    add(PELang.Keys.CONTAINER_PRECISION_GRINDSTONE, "Precision Grindstone");
    add(PELang.Keys.SCREEN_SELECT_NEXT, "Select next");
    add(PELang.Keys.SCREEN_SELECT_PREV, "Select previous");
    add(PELang.Keys.SCREEN_LOADING, "Loading");
    add(PELang.Keys.TABLET_SEARCH_HINT, "Search");
    add(PELang.Keys.TABLET_SYNC_FAILED, "Failed to sync recipes");
    add(PELang.Keys.NO_IMPOSSIBLE_RECIPES, "No impossible recipes found");
    add(PELang.Keys.FOUND_IMPOSSIBLE_RECIPES, "%s impossible recipe(s) found:");
    add(PELang.Keys.MORE_IMPOSSIBLE_RECIPES, "... +%s more");
  }

}
