package me.whizvox.precisionenchanter.common.menu;

import me.whizvox.precisionenchanter.common.api.EnchantmentStorageManager;
import me.whizvox.precisionenchanter.common.api.IEnchantmentStorage;
import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import me.whizvox.precisionenchanter.common.registry.PEMenus;
import me.whizvox.precisionenchanter.common.util.MenuUtil;
import me.whizvox.precisionenchanter.common.util.PEEnchantmentHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrecisionGrindstoneMenu extends AbstractContainerMenu {

  private final ContainerLevelAccess access;
  private final IItemHandlerModifiable resultSlots;
  private final IItemHandlerModifiable scrapeFromSlots;
  private final IItemHandlerModifiable applyOntoSlots;

  // server
  private final List<EnchantmentInstance> enchantments;
  private int selectedEnchantment;
  private IEnchantmentStorage scrapedStorage;
  private IEnchantmentStorage appliedStorage;

  private final DataSlot enchantmentId;
  private final DataSlot enchantmentLevel;
  private final DataSlot hasMultiple;

  private boolean preventResultUpdates;

  private static final int
      BOOL_FALSE = 1,
      BOOL_TRUE = 0;

  // client
  public PrecisionGrindstoneMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
    this(containerId, playerInv, ContainerLevelAccess.NULL);
  }

  // server
  public PrecisionGrindstoneMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
    super(PEMenus.PRECISION_GRINDSTONE.get(), containerId);
    this.access = access;

    resultSlots = new ItemStackHandler(1);
    scrapeFromSlots = new ItemStackHandler(1) {
      @Override
      protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        updateResult();
      }
    };
    applyOntoSlots = new ItemStackHandler(1) {
      @Override
      protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        updateResult();
      }
    };

    addSlot(new SlotItemHandler(resultSlots, 0, 127, 40) {
      @Override
      public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
      }
      @Override
      public void onTake(Player player, ItemStack stack) {
        access.execute((level, pos) -> {
          preventResultUpdates = true;
          scrapedStorage.removeEnchantment(scrapeFromSlots.getStackInSlot(0), enchantments.get(selectedEnchantment).enchantment);
          scrapedStorage.postRemoveEnchantment(scrapeFromSlots.getStackInSlot(0));
          appliedStorage.postApplyEnchantment(applyOntoSlots.getStackInSlot(0));
          preventResultUpdates = false;
          updateResult();
          player.playNotifySound(SoundEvents.AXE_SCRAPE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        });
      }
    });
    addSlot(new SlotItemHandler(scrapeFromSlots, 0, 29, 40));
    addSlot(new SlotItemHandler(applyOntoSlots, 0, 69, 40));

    MenuUtil.addPlayerInventory(playerInv, this::addSlot);

    int[] sharedSlots = new int[3];
    enchantmentId = DataSlot.shared(sharedSlots, 0);
    enchantmentLevel = DataSlot.shared(sharedSlots, 1);
    hasMultiple = DataSlot.shared(sharedSlots, 2);

    enchantmentId.set(-1);
    hasMultiple.set(BOOL_FALSE);

    addDataSlot(enchantmentId);
    addDataSlot(enchantmentLevel);
    addDataSlot(hasMultiple);

    enchantments = new ArrayList<>();
    selectedEnchantment = 0;
    preventResultUpdates = false;
  }

  private void updateResult() {
    access.execute((level, pos) -> {
      if (preventResultUpdates) {
        return;
      }
      preventResultUpdates = true;
      enchantments.clear();
      selectedEnchantment = 0;
      scrapedStorage = null;
      appliedStorage = null;
      boolean clearResult = true;
      ItemStack scrapeFrom = scrapeFromSlots.getStackInSlot(0);
      ItemStack applyOnto = applyOntoSlots.getStackInSlot(0);
      if (!scrapeFrom.isEmpty() && !applyOnto.isEmpty()) {
        IEnchantmentStorage scrapeFromStorage = EnchantmentStorageManager.INSTANCE.findMatch(scrapeFrom);
        IEnchantmentStorage applyOntoStorage = EnchantmentStorageManager.INSTANCE.findMatch(applyOnto);
        if (scrapeFromStorage != null && applyOntoStorage != null) {
          Map<Enchantment, Integer> scrapeFromEnchantments = scrapeFromStorage.getEnchantments(scrapeFrom);
          if (!scrapeFromEnchantments.isEmpty()) {
            scrapeFromEnchantments.forEach((enchantment, enchLevel) -> {
              EnchantmentInstance inst = new EnchantmentInstance(enchantment, enchLevel);
              if (applyOntoStorage.canApply(applyOnto, inst)) {
                enchantments.add(inst);
              }
            });
            if (!enchantments.isEmpty()) {
              clearResult = false;
              scrapedStorage = scrapeFromStorage;
              appliedStorage = applyOntoStorage;
              changeSelection(0);
            }
          }
        }
      }
      if (clearResult) {
        enchantmentId.set(-1);
        enchantmentLevel.set(0);
        resultSlots.setStackInSlot(0, ItemStack.EMPTY);
        broadcastChanges();
      }
      preventResultUpdates = false;
    });
  }

  public boolean hasMultipleEnchantments() {
    return hasMultiple.get() == BOOL_TRUE;
  }

  public boolean enchantmentEquals(EnchantmentInstance other) {
    return other == null ?
        enchantmentId.get() == -1 :
        PEEnchantmentHelper.INSTANCE.getId(other.enchantment) == enchantmentId.get() && other.level == enchantmentLevel.get() + 1;
  }

  @Nullable
  public EnchantmentInstance getSelectedEnchantment() {
    int id = enchantmentId.get();
    if (id > -1) {
      Enchantment enchantment = PEEnchantmentHelper.INSTANCE.get(id);
      if (enchantment != null) {
        return new EnchantmentInstance(enchantment, enchantmentLevel.get() + 1);
      } else {
        PELog.LOGGER.warn(PELog.side(), "Could not determine enchantment from ID: {}", id);
      }
    }
    return null;
  }

  public void changeSelection(int amount) {
    access.execute((level, blockPos) -> {
      if (enchantments.isEmpty()) {
        return;
      }
      selectedEnchantment = Math.abs((selectedEnchantment + amount) % enchantments.size());
      var selected = enchantments.get(selectedEnchantment);
      enchantmentId.set(PEEnchantmentHelper.INSTANCE.getId(selected.enchantment));
      enchantmentLevel.set(selected.level - 1);
      resultSlots.setStackInSlot(0, appliedStorage.applyEnchantment(applyOntoSlots.getStackInSlot(0), getSelectedEnchantment()));
      broadcastChanges();
    });
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    return MenuUtil.quickMoveStack(this, player, slotIndex, this::moveItemStackTo, 1, 2);
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(access, player, PEBlocks.PRECISION_GRINDSTONE.get());
  }

}
