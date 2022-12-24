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
    addItem(PEItems.QUILL, "Quill");
    addItem(PEItems.ENCHANTED_QUILL, "Enchanted Quill");
    add(PELang.Keys.CREATIVE_MODE_TAB, "Precision Enchanting");
    add(PELang.Keys.ENCHANTERS_WORKBENCH, "Enchanter's Workbench");
    add(PELang.Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_NEXT, "Select next");
    add(PELang.Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_PREV, "Select previous");
  }

}
