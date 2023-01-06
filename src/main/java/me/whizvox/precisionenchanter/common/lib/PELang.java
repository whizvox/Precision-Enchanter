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
        SCREEN_ENCHANTERS_WORKBENCH_SELECT_PREV = withModId("screen.%s.generic.select_previous"),
        SCREEN_ENCHANTERS_WORKBENCH_SELECT_NEXT = withModId("screen.%s.generic.select_next");
  }

  public static final Component
      CREATIVE_MODE_TAB = Component.translatable(Keys.CREATIVE_MODE_TAB),
      CONTAINER_ENCHANTERS_WORKBENCH = Component.translatable(Keys.CONTAINER_ENCHANTERS_WORKBENCH),
      CONTAINER_PRECISION_GRINDSTONE = Component.translatable(Keys.CONTAINER_PRECISION_GRINDSTONE),
      SCREEN_SELECT_PREV = Component.translatable(Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_PREV),
      SCREEN_SELECT_NEXT = Component.translatable(Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_NEXT);

}
