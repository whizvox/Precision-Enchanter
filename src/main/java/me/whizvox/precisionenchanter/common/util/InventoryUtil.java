package me.whizvox.precisionenchanter.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class InventoryUtil {

  /**
   * Create a copy of some inventory. Also copies the ItemStacks, so modifying the copy will not modify the original.
   * @param src The inventory to copy
   * @return A modifiable deeply-copied inventory
   */
  public static IItemHandlerModifiable copyInventory(IItemHandler src) {
    ItemStackHandler copy = new ItemStackHandler(src.getSlots());
    for (int i = 0; i < src.getSlots(); i++) {
      copy.setStackInSlot(i, src.getStackInSlot(i).copy());
    }
    return copy;
  }

  /**
   * Attempt to insert a collection of items into an inventory.
   * @param items The collection of items to insert
   * @param dest The destination inventory to insert into
   * @param copyItems Whether to copy each item before inserting it
   * @return Any items remaining after inserting into the destination inventory. Will be empty if all items were
   * successfully inserted.
   */
  public static List<ItemStack> insertAll(Collection<ItemStack> items, IItemHandlerModifiable dest, boolean copyItems) {
    Queue<ItemStack> leftovers;
    if (copyItems) {
      leftovers = new ArrayDeque<>();
      items.forEach(stack -> leftovers.add(stack.copy()));
    } else {
      leftovers = new ArrayDeque<>(items);
    }
    ItemStack leftover;
    for (int i = 0; i < dest.getSlots() && !leftovers.isEmpty(); i++) {
      leftover = dest.insertItem(i, leftovers.peek(), false);
      if (leftover.isEmpty()) {
        leftovers.remove();
      }
    }
    if (leftovers.isEmpty()) {
      return List.of();
    }
    return List.copyOf(leftovers);
  }

  /**
   * @see #insertAll(Collection, IItemHandlerModifiable, boolean)
   */
  public static List<ItemStack> insertAll(IItemHandler src, IItemHandlerModifiable dest, boolean copyItems) {
    List<ItemStack> items = new ArrayList<>(src.getSlots());
    for (int i = 0; i < src.getSlots(); i++) {
      items.add(src.getStackInSlot(i));
    }
    return insertAll(items, dest, copyItems);
  }

  public static void setAuthorId(ItemStack stack, Player player) {
    stack.addTagElement("AuthorID", NbtUtils.createUUID(player.getUUID()));
  }

  public static UUID getAuthorId(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag != null && tag.contains("AuthorID")) {
      return tag.getUUID("AuthorID");
    }
    return null;
  }

}
