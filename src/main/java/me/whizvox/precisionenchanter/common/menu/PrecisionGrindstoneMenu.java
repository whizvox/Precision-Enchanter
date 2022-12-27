package me.whizvox.precisionenchanter.common.menu;

import me.whizvox.precisionenchanter.common.registry.PEMenus;
import me.whizvox.precisionenchanter.common.util.MenuUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PrecisionGrindstoneMenu extends AbstractContainerMenu {

  private final ContainerLevelAccess access;
  private final IItemHandlerModifiable outputSlots;
  private final IItemHandlerModifiable scrapeFromSlots;
  private final IItemHandlerModifiable applyOntoSlots;

  // client
  public PrecisionGrindstoneMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
    this(containerId, playerInv, ContainerLevelAccess.NULL);
  }

  // server
  public PrecisionGrindstoneMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
    super(PEMenus.PRECISION_GRINDSTONE.get(), containerId);
    this.access = access;
    outputSlots = new ItemStackHandler(1);
    scrapeFromSlots = new ItemStackHandler(1);
    applyOntoSlots = new ItemStackHandler(1);

    addSlot(new SlotItemHandler(outputSlots, 0, 127, 35));
    addSlot(new SlotItemHandler(scrapeFromSlots, 0, 29, 35));
    addSlot(new SlotItemHandler(applyOntoSlots, 0, 69, 35));

    MenuUtil.addPlayerInventory(playerInv, this::addSlot);
  }

  @Override
  public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
    return null;
  }

  @Override
  public boolean stillValid(Player pPlayer) {
    return false;
  }

}
