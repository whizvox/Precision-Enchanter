package me.whizvox.precisionenchanter.common.lib;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import static me.whizvox.precisionenchanter.common.util.ChatUtil.ERROR;

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
      CREATIVE_MODE_TAB = new TranslatableComponent(Keys.CREATIVE_MODE_TAB),
      CONTAINER_ENCHANTERS_WORKBENCH = new TranslatableComponent(Keys.CONTAINER_ENCHANTERS_WORKBENCH),
      CONTAINER_PRECISION_GRINDSTONE = new TranslatableComponent(Keys.CONTAINER_PRECISION_GRINDSTONE),
      SCREEN_SELECT_PREV = new TranslatableComponent(Keys.SCREEN_SELECT_PREV),
      SCREEN_SELECT_NEXT = new TranslatableComponent(Keys.SCREEN_SELECT_NEXT),
      SCREEN_LOADING = new TranslatableComponent(Keys.SCREEN_LOADING),
      TABLET_SEARCH_HINT = new TranslatableComponent(Keys.TABLET_SEARCH_HINT),
      TABLET_SYNC_FAILED = new TranslatableComponent(Keys.TABLET_SYNC_FAILED),
      TABLET_SHOW_ALL = new TranslatableComponent(Keys.TABLET_SHOW_ALL),
      TABLET_SHOW_CRAFTABLE = new TranslatableComponent(Keys.TABLET_SHOW_CRAFTABLE),
      NO_IMPOSSIBLE_RECIPES = new TranslatableComponent(Keys.NO_IMPOSSIBLE_RECIPES),
      NO_FREE_RECIPES = new TranslatableComponent(Keys.NO_FREE_RECIPES);

  public static MutableComponent nMore(int count) {
    return new TranslatableComponent(Keys.N_MORE, count);
  }

  public static MutableComponent foundImpossibleRecipes(int count) {
    return new TranslatableComponent(Keys.FOUND_IMPOSSIBLE_RECIPES, ChatUtil.mut(count).withStyle(ERROR));
  }

  public static MutableComponent foundFreeRecipes(int count) {
    return new TranslatableComponent(Keys.FOUND_FREE_RECIPES, ChatUtil.mut(count).withStyle(ERROR));
  }

}
