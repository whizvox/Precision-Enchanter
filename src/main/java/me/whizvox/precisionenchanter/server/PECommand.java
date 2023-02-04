package me.whizvox.precisionenchanter.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class PECommand {

  private static final int SUCCESS = Command.SINGLE_SUCCESS;

  private static int checkForImpossibleRecipes(CommandSourceStack src) {
    var recipes = EnchantmentRecipeManager.INSTANCE.findImpossibleRecipes();
    if (recipes.isEmpty()) {
      src.sendSystemMessage(PELang.NO_IMPOSSIBLE_RECIPES);
    } else {
      src.sendSystemMessage(PELang.foundImpossibleRecipes(recipes.size()));
      int currentCount = 0;
      for (EnchantmentRecipe recipe : recipes) {
        src.sendSystemMessage(Component.literal("- ").append(ChatUtil.mut(recipe.getId()).withStyle(ChatUtil.SECONDARY)));
        currentCount++;
        if (currentCount > 5) {
          src.sendSystemMessage(Component.literal("- ").append(PELang.moreImpossibleRecipes(recipes.size() - currentCount)));
        }
      }
    }
    return SUCCESS;
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    var mainCommand = Commands.literal("precisionenchanting")
            .then(Commands.literal("checkrecipes")
                .requires(src -> src.hasPermission(1))
                .executes(ctx -> checkForImpossibleRecipes(ctx.getSource()))
            );
    dispatcher.register(mainCommand);
  }

}
