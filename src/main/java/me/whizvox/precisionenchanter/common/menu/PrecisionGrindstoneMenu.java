package me.whizvox.precisionenchanter.common.menu;

import me.whizvox.precisionenchanter.common.api.EnchantmentStorageManager;
import me.whizvox.precisionenchanter.common.api.IEnchantmentStorage;
import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import me.whizvox.precisionenchanter.common.registry.PEMenus;
import me.whizvox.precisionenchanter.common.util.MenuUtil;
import me.whizvox.precisionenchanter.common.util.PEEnchantmentHelper;
import me.whizvox.precisionenchanter.common.util.PEMathUtil;
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
import net.minecraftforge.items.wrapper.RecipeWrapper;
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
  private final DataSlot cost;

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

    addSlot(new SlotItemHandler(resultSlots, 0, 127, 39) {
      @Override
      public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
      }
      @Override
      public boolean mayPickup(Player player) {
        return EnchantmentRecipe.canCraftEnchantment(player, cost.get());
      }
      @Override
      public void onTake(Player player, ItemStack stack) {
        access.execute((level, pos) -> {
          if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-cost.get());
          }
          ItemStack scrapeFrom = scrapeFromSlots.getStackInSlot(0);
          ItemStack applyTo = applyOntoSlots.getStackInSlot(0);
          preventResultUpdates = true;
          scrapeFromSlots.setStackInSlot(0, scrapedStorage.removeEnchantment(scrapeFrom, enchantments.get(selectedEnchantment).enchantment));
          scrapedStorage.postRemoveEnchantment(scrapeFrom);
          appliedStorage.postApplyEnchantment(applyTo);
          preventResultUpdates = false;
          updateResult();
          player.playNotifySound(SoundEvents.GRINDSTONE_USE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        });
      }
    });
    addSlot(new SlotItemHandler(scrapeFromSlots, 0, 29, 39));
    addSlot(new SlotItemHandler(applyOntoSlots, 0, 69, 39));

    MenuUtil.addPlayerInventory(playerInv, this::addSlot);

    int[] sharedSlots = new int[4];
    enchantmentId = DataSlot.shared(sharedSlots, 0);
    enchantmentLevel = DataSlot.shared(sharedSlots, 1);
    hasMultiple = DataSlot.shared(sharedSlots, 2);
    cost = DataSlot.shared(sharedSlots, 3);

    enchantmentId.set(-1);
    hasMultiple.set(BOOL_FALSE);

    addDataSlot(enchantmentId);
    addDataSlot(enchantmentLevel);
    addDataSlot(hasMultiple);
    addDataSlot(cost);

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
              hasMultiple.set(enchantments.size() > 1 ? BOOL_TRUE : BOOL_FALSE);
              changeSelection(0);
            }
          }
        }
      }
      if (clearResult) {
        enchantmentId.set(-1);
        enchantmentLevel.set(0);
        hasMultiple.set(BOOL_FALSE);
        resultSlots.setStackInSlot(0, ItemStack.EMPTY);
        cost.set(0);
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
        PEEnchantmentHelper.INSTANCE.getId(other.enchantment) == enchantmentId.get() && other.level == enchantmentLevel.get();
  }

  public int getCost() {
    return cost.get();
  }

  @Nullable
  public EnchantmentInstance getSelectedEnchantment() {
    int id = enchantmentId.get();
    if (id > -1) {
      Enchantment enchantment = PEEnchantmentHelper.INSTANCE.get(id);
      if (enchantment != null) {
        return new EnchantmentInstance(enchantment, enchantmentLevel.get());
      } else {
        PELog.LOGGER.warn("Could not determine enchantment from ID: {}", id);
      }
    }
    return null;
  }

  public void changeSelection(int amount) {
    access.execute((level, blockPos) -> {
      if (enchantments.isEmpty()) {
        return;
      }
      selectedEnchantment = PEMathUtil.rollover(selectedEnchantment, enchantments.size(), amount);
      var selected = enchantments.get(selectedEnchantment);
      enchantmentId.set(PEEnchantmentHelper.INSTANCE.getId(selected.enchantment));
      enchantmentLevel.set(selected.level);
      resultSlots.setStackInSlot(0, appliedStorage.applyEnchantment(applyOntoSlots.getStackInSlot(0), getSelectedEnchantment()));
      EnchantmentRecipe recipe = EnchantmentRecipeManager.INSTANCE.get(selected.enchantment, selected.level);
      int costAmount;
      if (recipe == null) {
        costAmount = switch (selected.enchantment.getRarity()) {
          case COMMON -> 1;
          case UNCOMMON -> 2;
          case RARE -> 3;
          case VERY_RARE -> 4;
        };
      } else {
        costAmount = recipe.getGrindstoneCost();
      }
      cost.set(costAmount);
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

  @Override
  public void removed(Player player) {
    super.removed(player);
    clearContainer(player, new RecipeWrapper(scrapeFromSlots));
    clearContainer(player, new RecipeWrapper(applyOntoSlots));
  }

}
