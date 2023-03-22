package me.whizvox.precisionenchanter.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.whizvox.precisionenchanter.PrecisionEnchanter;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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
    return SUCCESS;
  }

  private static final ResourceLocation[] IGNORED_ENCHANTMENTS = new ResourceLocation[] {
      new ResourceLocation("apotheosis", "infusion") // placeholder enchantment for Apotheosis
  };

  /*static {
    Arrays.sort(IGNORED_ENCHANTMENTS);
  }*/

  private static int checkForUnavailableRecipes(CommandSourceStack src) {
    List<EnchantmentInstance> unavailable = new ArrayList<>();
    ForgeRegistries.ENCHANTMENTS.getValues().forEach(enchantment -> {
      ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
      if (Arrays.binarySearch(IGNORED_ENCHANTMENTS, id) < 0) {
        for (int level = 1; level <= enchantment.getMaxLevel(); level++) {
          if (EnchantmentRecipeManager.INSTANCE.get(enchantment, level) == null) {
            unavailable.add(new EnchantmentInstance(enchantment, level));
          }
        }
      }
    });
    if (unavailable.isEmpty()) {
      src.sendSystemMessage(PELang.NO_MISSING_RECIPES);
    } else {
      src.sendSystemMessage(PELang.foundMissingRecipes(unavailable.size()));
      listItems(src, unavailable.stream().map(inst -> ForgeRegistries.ENCHANTMENTS.getKey(inst.enchantment) + " " + inst.level).toList(), MAX_LIST_COUNT, Style.EMPTY.withColor(ChatUtil.SECONDARY));
    }
    return SUCCESS;
  }

  @SafeVarargs
  private static int runMultiple(CommandSourceStack src, Function<CommandSourceStack, Integer>... commands) {
    for (Function<CommandSourceStack, Integer> command : commands) {
      int ret = command.apply(src);
      if (ret != SUCCESS) {
        return ret;
      }
    }
    return SUCCESS;
  }

  private static int checkRecipes(CommandSourceStack src) {
    return runMultiple(src, PECommand::checkForImpossibleRecipes, PECommand::checkForFreeRecipes, PECommand::checkForUnavailableRecipes);
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    var mainCommand = Commands.literal(PrecisionEnchanter.MOD_ID)
            .then(Commands.literal("checkrecipes")
                .requires(src -> src.hasPermission(1))
                .executes(ctx -> checkRecipes(ctx.getSource()))
                .then(Commands.literal("impossible").executes(ctx -> checkForImpossibleRecipes(ctx.getSource())))
                .then(Commands.literal("free").executes(ctx -> checkForFreeRecipes(ctx.getSource())))
                .then(Commands.literal("unavailable").executes(ctx -> checkForUnavailableRecipes(ctx.getSource())))
            );
    dispatcher.register(mainCommand);
  }

}
