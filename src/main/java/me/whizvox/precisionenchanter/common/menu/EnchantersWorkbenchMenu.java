package me.whizvox.precisionenchanter.common.menu;

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
import net.minecraft.world.Container;
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
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnchantersWorkbenchMenu extends AbstractContainerMenu {

  private final ContainerLevelAccess access;
  private final IItemHandlerModifiable enchantableInputSlots;
  private final IItemHandlerModifiable ingredientInputSlots;
  private final IItemHandlerModifiable resultSlots;
  private final Container ingredientInputContainer;

  // server
  private final List<Pair<EnchantmentRecipe, EnchantmentRecipe.MatchResult>> matches;
  private int selectedRecipe;
  private boolean preventResultUpdates;

  // common
  private final DataSlot cost;
  private final DataSlot enchantmentId;
  private final DataSlot enchantmentLevel;
  private final DataSlot matchesMultiple;

  private static final int
      BOOL_FALSE = 1,
      BOOL_TRUE = 0;

  // client
  public EnchantersWorkbenchMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
    this(containerId, playerInv, ContainerLevelAccess.NULL);
  }

  // server
  public EnchantersWorkbenchMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
    super(PEMenus.ENCHANTERS_WORKBENCH.get(), containerId);
    this.access = access;
    selectedRecipe = 0;

    int[] slots = new int[4];
    cost = DataSlot.shared(slots, 0);
    enchantmentId = DataSlot.shared(slots, 1);
    enchantmentLevel = DataSlot.shared(slots, 2);
    matchesMultiple = DataSlot.shared(slots, 3);

    enchantmentId.set(-1);
    matchesMultiple.set(BOOL_FALSE);

    addDataSlot(cost);
    addDataSlot(enchantmentId);
    addDataSlot(enchantmentLevel);
    addDataSlot(matchesMultiple);

    enchantableInputSlots = new ItemStackHandler(1) {
      @Override
      protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        updateResult();
      }
    };
    ingredientInputSlots = new ItemStackHandler(4) {
      @Override
      protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        updateResult();
      }
    };
    resultSlots = new ItemStackHandler(1);
    ingredientInputContainer = new RecipeWrapper(ingredientInputSlots);

    // result slot
    addSlot(new SlotItemHandler(resultSlots, 0, 136, 39) {
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
          preventResultUpdates = true;
          if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-cost.get());
          }
          enchantableInputSlots.getStackInSlot(0).shrink(1);
          for (int i = 0; i < ingredientInputSlots.getSlots(); i++) {
            ingredientInputSlots.setStackInSlot(i, matches.get(selectedRecipe).getRight().leftoverIngredients().get(i));
          }
          preventResultUpdates = false;
          updateResult();
          player.playNotifySound(SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        });
      }
    });
    // stack to enchant slot
    addSlot(new SlotItemHandler(enchantableInputSlots, 0, 20, 39));
    // ingredient slots
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        addSlot(new SlotItemHandler(ingredientInputSlots, j + i * 2, 60 + j * 18, 30 + i * 18));
      }
    }

    MenuUtil.addPlayerInventory(playerInv, this::addSlot);

    matches = new ArrayList<>();
    preventResultUpdates = false;
  }

  private void updateResult() {
    access.execute((level, pos) -> {
      if (preventResultUpdates) {
        return;
      }
      preventResultUpdates = true;
      matches.clear();
      selectedRecipe = 0;
      ItemStack stackToEnchant = enchantableInputSlots.getStackInSlot(0);
      boolean clearOutput = true;
      if (!stackToEnchant.isEmpty()) {
        Container container = new RecipeWrapper(ingredientInputSlots);
        var matchedRecipes = EnchantmentRecipeManager.INSTANCE.match(stackToEnchant, container);
        matches.clear();
        if (!matchedRecipes.isEmpty()) {
          clearOutput = false;
          matches.addAll(matchedRecipes);
          // sort in descending order according to how much an enchantment instance is "worth", estimated via its cost
          matches.sort((o1, o2) -> o2.getRight().cost() - o1.getRight().cost());
          if (matches.size() > 1) {
            matchesMultiple.set(BOOL_TRUE);
          } else {
            matchesMultiple.set(BOOL_FALSE);
          }
          changeSelection(0);
        }
      }
      if (clearOutput) {
        cost.set(0);
        matchesMultiple.set(BOOL_FALSE);
        enchantmentId.set(-1);
        enchantmentLevel.set(0);
        resultSlots.setStackInSlot(0, ItemStack.EMPTY);
        broadcastChanges();
      }
      preventResultUpdates = false;
    });
  }

  public void clearIngredients(Player player) {
    clearContainer(player, ingredientInputContainer);
  }

  public void changeSelection(int amount) {
    access.execute((level, blockPos) -> {
      if (matches.isEmpty()) {
        return;
      }
      selectedRecipe = PEMathUtil.rollover(selectedRecipe, matches.size(), amount);
      var result = matches.get(selectedRecipe);
      cost.set(result.getRight().cost());
      enchantmentId.set(PEEnchantmentHelper.INSTANCE.getId(result.getLeft().getEnchantment()));
      enchantmentLevel.set(result.getLeft().getLevel());
      resultSlots.setStackInSlot(0, result.getRight().result());
      broadcastChanges();
    });
  }

  public boolean enchantmentsEquals(EnchantmentInstance enchantment) {
    return enchantment == null ?
        enchantmentId.get() == -1 :
        PEEnchantmentHelper.INSTANCE.getId(enchantment.enchantment) == enchantmentId.get() && enchantment.level == enchantmentLevel.get();
  }

  @Nullable
  public EnchantmentInstance getSelectedEnchantment() {
    int id = enchantmentId.get();
    if (id > -1) {
      Enchantment enchantment = PEEnchantmentHelper.INSTANCE.get(enchantmentId.get());
      if (enchantment == null) {
        PELog.LOGGER.warn(PELog.side(), "Could not determine enchantment from numerical ID: {}", id);
      } else {
        return new EnchantmentInstance(enchantment, enchantmentLevel.get());
      }
    }
    return null;
  }

  public int getCost() {
    return cost.get();
  }

  public boolean multipleRecipesMatched() {
    return matchesMultiple.get() == BOOL_TRUE;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    return MenuUtil.quickMoveStack(this, player, slotIndex, this::moveItemStackTo, 1, 5);
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(access, player, PEBlocks.ENCHANTERS_WORKBENCH.get());
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    clearContainer(player, new RecipeWrapper(enchantableInputSlots));
    clearContainer(player, ingredientInputContainer);
  }

}
