package me.whizvox.precisionenchanter.common.lib;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static me.whizvox.precisionenchanter.common.util.ChatUtil.ERROR;

public class PELang {

  private static String withModId(String format) {
    return String.format(format, PrecisionEnchanter.MOD_ID);
  }

  public static class Keys {
    public static final String
        CREATIVE_MODE_TAB = withModId("itemGroup.%s.main"),
        CONTAINER_WORKBENCH_TITLE = withModId("container.%s.enchanters_workbench"),
        CONTAINER_GRINDSTONE_TITLE = withModId("container.%s.precision_grindstone"),
        SCREEN_SELECT_PREV = withModId("screen.%s.generic.select_previous"),
        SCREEN_SELECT_NEXT = withModId("screen.%s.generic.select_next"),
        SCREEN_LOADING = withModId("screen.%s.generic.loading"),
        WORKBENCH_SHOW_RECIPES = withModId("screen.%s.enchanters_workbench.show_recipes"),
        WORKBENCH_HIDE_RECIPES = withModId("screen.%s.enchanters_workbench.hide_recipes"),
        TABLET_SEARCH_HINT = withModId("screen.%s.enchantment_recipe_tablet.search_hint"),
        TABLET_SYNC_FAILED = withModId("screen.%s.enchantment_recipe_tablet.sync_failed"),
        TABLET_SHOW_ALL = withModId("screen.%s.enchantment_recipe_tablet.show_all"),
        TABLET_SHOW_CRAFTABLE = withModId("screen.%s.enchantment_recipe_tablet.show_craftables"),

        // Player chat
        N_MORE = withModId("command.%s.generic.more"),
        NO_IMPOSSIBLE_RECIPES = withModId("command.%s.check_recipes.impossible.none"),
        FOUND_IMPOSSIBLE_RECIPES = withModId("command.%s.check_recipes.impossible.header"),
        NO_FREE_RECIPES = withModId("command.%s.check_recipes.free.none"),
        FOUND_FREE_RECIPES = withModId("command.%s.check_recipes.free.header");
  }

  public static final Component
      CREATIVE_MODE_TAB = Component.translatable(Keys.CREATIVE_MODE_TAB),
      CONTAINER_ENCHANTERS_WORKBENCH = Component.translatable(Keys.CONTAINER_WORKBENCH_TITLE),
      CONTAINER_PRECISION_GRINDSTONE = Component.translatable(Keys.CONTAINER_GRINDSTONE_TITLE),
      SCREEN_SELECT_PREV = Component.translatable(Keys.SCREEN_SELECT_PREV),
      SCREEN_SELECT_NEXT = Component.translatable(Keys.SCREEN_SELECT_NEXT),
      SCREEN_LOADING = Component.translatable(Keys.SCREEN_LOADING),
      WORKBENCH_SHOW_RECIPES = Component.translatable(Keys.WORKBENCH_SHOW_RECIPES),
      WORKBENCH_HIDE_RECIPES = Component.translatable(Keys.WORKBENCH_HIDE_RECIPES),
      TABLET_SEARCH_HINT = Component.translatable(Keys.TABLET_SEARCH_HINT),
      TABLET_SYNC_FAILED = Component.translatable(Keys.TABLET_SYNC_FAILED),
      TABLET_SHOW_ALL = Component.translatable(Keys.TABLET_SHOW_ALL),
      TABLET_SHOW_CRAFTABLE = Component.translatable(Keys.TABLET_SHOW_CRAFTABLE),
      NO_IMPOSSIBLE_RECIPES = Component.translatable(Keys.NO_IMPOSSIBLE_RECIPES),
      NO_FREE_RECIPES = Component.translatable(Keys.NO_FREE_RECIPES);

  public static MutableComponent nMore(int count) {
    return Component.translatable(Keys.N_MORE, count);
  }

  public static MutableComponent foundImpossibleRecipes(int count) {
    return Component.translatable(Keys.FOUND_IMPOSSIBLE_RECIPES, ChatUtil.mut(count).withStyle(ERROR));
  }

  public static MutableComponent foundFreeRecipes(int count) {
    return Component.translatable(Keys.FOUND_FREE_RECIPES, ChatUtil.mut(count).withStyle(ERROR));
  }

}
