package me.whizvox.precisionenchanter.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class MenuUtil {

  public static boolean isValid(Player player, BlockPos pos) {
    // 8 blocks away
    return pos.distToCenterSqr(player.position()) <= 64.0;
  }

  public static void addPlayerInventory(Inventory playerInv, Consumer<Slot> slotAdder, int xOff, int yOff) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        slotAdder.accept(new Slot(playerInv, 9 + j + i * 9, xOff + j * 18, yOff + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      slotAdder.accept(new Slot(playerInv, i, xOff + i * 18, yOff + 58));
    }
  }

  public static void addPlayerInventory(Inventory playerInv, Consumer<Slot> slotAdder) {
    addPlayerInventory(playerInv, slotAdder, 8, 84);
  }

  public interface MoveItemStackToFunction {

    boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection);

  }

  public static ItemStack quickMoveStack(AbstractContainerMenu menu, Player player, int slotIndex, MoveItemStackToFunction func, int numOutputSlots, int numInputSlots) {
    int outputSlotStart = 0;
    int outputSlotEnd = outputSlotStart + numOutputSlots;
    int inputStartSlot = outputSlotEnd + numOutputSlots;
    int inputEndSlot = inputStartSlot + numInputSlots;
    int invStart = inputEndSlot;
    int invEnd = invStart + 27;
    int hotbarStart = invEnd;
    int hotbarEnd = hotbarStart + 9;
    ItemStack origStack = ItemStack.EMPTY;
    Slot clickedSlot = menu.slots.get(slotIndex);
    if (clickedSlot.hasItem()) {
      ItemStack stack = clickedSlot.getItem();
      origStack = stack.copy();
      if (slotIndex >= outputSlotStart && slotIndex < outputSlotEnd) {
        if (!func.moveItemStackTo(stack, invStart, hotbarEnd, true)) {
          return ItemStack.EMPTY;
        }
        clickedSlot.onQuickCraft(stack, origStack);
      } else if (slotIndex >= invStart && slotIndex < hotbarEnd) {
        if (!func.moveItemStackTo(stack, inputStartSlot, inputEndSlot, false)) {
          if (slotIndex < hotbarStart) {
            if (func.moveItemStackTo(stack, hotbarStart, hotbarEnd, false)) {
              return ItemStack.EMPTY;
            }
          } else if (!func.moveItemStackTo(stack, invStart, invEnd, false)) {
            return ItemStack.EMPTY;
          }
        }
      } else if (!func.moveItemStackTo(stack, invStart, hotbarEnd, false)) {
        return ItemStack.EMPTY;
      }
      if (stack.isEmpty()) {
        clickedSlot.set(ItemStack.EMPTY);
      } else {
        clickedSlot.setChanged();
      }
      if (origStack.getCount() == stack.getCount()) {
        return ItemStack.EMPTY;
      }
      clickedSlot.onTake(player, stack);
      if (slotIndex >= outputSlotStart && slotIndex < outputSlotEnd) {
        player.drop(stack, false);
      }
    }
    return origStack;
  }

}
