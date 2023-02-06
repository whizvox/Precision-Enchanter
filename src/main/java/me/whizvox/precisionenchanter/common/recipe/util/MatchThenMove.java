package me.whizvox.precisionenchanter.common.recipe.util;

import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.util.InventoryUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public record MatchThenMove(boolean matches, IItemHandler inputInventory, IItemHandler outputInventory) {

  public static final MatchThenMove FALSE = new MatchThenMove(false, MatchResultUtil.EMPTY_INVENTORY, MatchResultUtil.EMPTY_INVENTORY);

  public static MatchThenMove attempt(EnchantmentRecipe recipe, IItemHandler inputInv, IItemHandler outputInv) {
    // This becomes quite complicated due to multiple stacks potentially matching the same ingredient (i.e. both
    // cobblestone and deepslate cobblestone match the #cobblestone tag). Consider the following scenario:
    // A recipe calls for 10 #cobblestone, there exists 1 cobblestone 20 deepslate cobblestone in the player's
    // inventory. There's enough room in the output for the 20 deepslate cobblestone, but not both the 1 cobblestone
    // and 9 deepslate cobblestone. So, rather than immediately selecting the first encountered ingredient match and
    // using that, it's better to sort all matched items by stack count in descending order and select from the largest
    // stack count first.

    // First attempt to clear out ingredients in output slots and put them all into the input slots
    IItemHandlerModifiable input = InventoryUtil.copyInventory(inputInv);
    // If there are any leftovers from doing this, stop any further processing.
    if (!InventoryUtil.insertAll(outputInv, input, true).isEmpty()) {
      return FALSE;
    }
    // Clean slate for the output slots.
    IItemHandlerModifiable output = new ItemStackHandler(outputInv.getSlots());

    // Sort ingredients by count in descending order
    List<Pair<Ingredient, Integer>> remaining = new ArrayList<>();
    recipe.getIngredients().forEach(pair -> remaining.add(new MutablePair<>(pair.getLeft(), pair.getRight())));
    remaining.sort((o1, o2) -> o2.getRight() - o1.getRight());

    // Find all slots that match any ingredients in the recipe
    List<ItemStack> matchingStacks = new ArrayList<>();
    for (int i = 0; i < input.getSlots(); i++) {
      ItemStack stack = input.getStackInSlot(i);
      if (!stack.isEmpty()) {
        for (Pair<Ingredient, Integer> pair : remaining) {
          if (pair.getLeft().test(stack)) {
            matchingStacks.add(stack);
          }
        }
      }
    }

    // Sort by count in descending order
    matchingStacks.sort((o1, o2) -> o2.getCount() - o1.getCount());

    // Iterate through the matching stacks
    for (ItemStack stack : matchingStacks) {
      // Iterate through ingredients
      for (int i = 0; i < remaining.size(); i++) {
        Pair<Ingredient, Integer> pair = remaining.get(i);
        Ingredient ingredient = pair.getLeft();
        if (ingredient.test(stack)) {
          // Create a copy of the stack (limited by the target count) and insert it into the output inventory
          int targetCount = pair.getRight();
          int actualCount = stack.getCount();
          ItemStack leftover = stack.copy();
          leftover.setCount(Math.min(targetCount, actualCount));
          for (int j = 0; j < output.getSlots(); j++) {
            // Contains the leftover stack from attempting to insert (i.e. inserting 64 cobblestone into a slot with
            // 16 cobblestone results in a stack of 48 cobblestone). If this leftover is empty, that means the entire
            // stack was successfully inserted, and we shouldn't need to check any other ingredients.
            leftover = output.insertItem(j, leftover, false);
            if (leftover.isEmpty()) {
              break;
            }
          }
          // If there was any leftover, then we couldn't insert into the output inventory due to it being full. This
          // is caused by multiple stacks matched a single ingredient.
          if (!leftover.isEmpty()) {
            return FALSE;
          }
          // Now shrink the actual stack, since we were working with just a copy.
          stack.shrink(targetCount);
          int diff = targetCount - actualCount;
          if (diff <= 0) {
            // Remove the ingredient from the list if its count has been fulfilled. This is used both to prevent
            // unnecessary tests against an already fulfilled ingredient, and it is used at the end of the method to
            // determine whether any ingredients remain in the list. If so, then this recipe doesn't have a full match.
            remaining.remove(pair);
            i--;
          } else {
            pair.setValue(diff);
          }
          // If the stack is empty, then it's not necessary to test against any other ingredients.
          if (stack.isEmpty()) {
            break;
          }
        }
      }
    }

    // If there are any ingredients remaining, then we couldn't find a full match for all of them.
    if (!remaining.isEmpty()) {
      return FALSE;
    }

    return new MatchThenMove(true, input, output);
  }

}
