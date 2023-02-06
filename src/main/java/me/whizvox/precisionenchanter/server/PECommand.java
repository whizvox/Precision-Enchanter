package me.whizvox.precisionenchanter.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.List;

public class PECommand {

  private static final int
      SUCCESS = Command.SINGLE_SUCCESS,
      MAX_LIST_COUNT = 5;

  private static MutableComponent createListEntry(Object obj, Style itemStyle) {
    return Component.literal("- ").append(ChatUtil.mut(obj).withStyle(itemStyle));
  }

  private static void listItems(CommandSourceStack src, List<?> items, int max, Style itemStyle) {
    for (int i = 0; i < items.size() && i < max; i++) {
      src.sendSystemMessage(createListEntry(items.get(i), itemStyle));
    }
    if (max < items.size()) {
      int currentCount = max;
      MutableComponent remainingItems = createListEntry(items.get(currentCount), itemStyle);
      currentCount++;
      while (currentCount < items.size()) {
        remainingItems.append(Component.literal("\n").append(createListEntry(items.get(currentCount), itemStyle)));
        currentCount++;
      }
      src.sendSystemMessage(Component.literal("- ")
          .append(PELang.nMore(items.size() - max).withStyle(
              Style.EMPTY
                  .withUnderlined(true)
                  .withColor(ChatUtil.INFO)
                  .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, remainingItems))
              )
          )
      );
    }
  }

  private static int checkForImpossibleRecipes(CommandSourceStack src) {
    var recipes = EnchantmentRecipeManager.INSTANCE.findImpossibleRecipes();
    if (recipes.isEmpty()) {
      src.sendSystemMessage(PELang.NO_IMPOSSIBLE_RECIPES);
    } else {
      src.sendSystemMessage(PELang.foundImpossibleRecipes(recipes.size()));
      listItems(src, recipes.stream().map(EnchantmentRecipe::getId).toList(), MAX_LIST_COUNT, Style.EMPTY.withColor(ChatUtil.SECONDARY));
    }
    return SUCCESS;
  }

  private static int checkForFreeRecipes(CommandSourceStack src) {
    var recipes = EnchantmentRecipeManager.INSTANCE.findFreeRecipes();
    if (recipes.isEmpty()) {
      src.sendSystemMessage(PELang.NO_FREE_RECIPES);
    } else {
      src.sendSystemMessage(PELang.foundFreeRecipes(recipes.size()));
      listItems(src, recipes.stream().map(EnchantmentRecipe::getId).toList(), MAX_LIST_COUNT, Style.EMPTY.withColor(ChatUtil.SECONDARY));
    }
    return 1;
  }

  private static int checkRecipes(CommandSourceStack src) {
    int ret;
    if ((ret = checkForImpossibleRecipes(src)) != 1) {
      return ret;
    }
    return checkForFreeRecipes(src);
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    var mainCommand = Commands.literal(PrecisionEnchanter.MOD_ID)
            .then(Commands.literal("checkrecipes")
                .requires(src -> src.hasPermission(1))
                .executes(ctx -> checkRecipes(ctx.getSource()))
            );
    dispatcher.register(mainCommand);
  }

}
