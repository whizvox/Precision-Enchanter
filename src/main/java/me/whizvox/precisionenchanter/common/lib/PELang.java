package me.whizvox.precisionenchanter.common.lib;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import net.minecraft.network.chat.Component;

public class PELang {

  private static String withModId(String format) {
    return String.format(format, PrecisionEnchanter.MOD_ID);
  }

  public static class Keys {
    public static final String
        CREATIVE_MODE_TAB = withModId("itemGroup.%s.main"),
        CONTAINER_ENCHANTERS_WORKBENCH = withModId("container.%s.enchanters_workbench"),
        CONTAINER_PRECISION_GRINDSTONE = withModId("container.%s.precision_grindstone"),
        SCREEN_SELECT_PREV = withModId("screen.%s.generic.select_previous"),
        SCREEN_SELECT_NEXT = withModId("screen.%s.generic.select_next"),
        SCREEN_LOADING = withModId("screen.%s.generic.loading"),
        TABLET_SEARCH_HINT = withModId("screen.%s.enchantment_recipe_tablet.search_hint"),
        TABLET_SYNC_FAILED = withModId("screen.%s.enchantment_recipe_tablet.sync_failed");
  }

  public static final Component
      CREATIVE_MODE_TAB = Component.translatable(Keys.CREATIVE_MODE_TAB),
      CONTAINER_ENCHANTERS_WORKBENCH = Component.translatable(Keys.CONTAINER_ENCHANTERS_WORKBENCH),
      CONTAINER_PRECISION_GRINDSTONE = Component.translatable(Keys.CONTAINER_PRECISION_GRINDSTONE),
      SCREEN_SELECT_PREV = Component.translatable(Keys.SCREEN_SELECT_PREV),
      SCREEN_SELECT_NEXT = Component.translatable(Keys.SCREEN_SELECT_NEXT),
      SCREEN_LOADING = Component.translatable(Keys.SCREEN_LOADING),
      TABLET_SEARCH_HINT = Component.translatable(Keys.TABLET_SEARCH_HINT),
      TABLET_SYNC_FAILED = Component.translatable(Keys.TABLET_SYNC_FAILED);

}
