package me.whizvox.precisionenchanter.client.screen;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.client.util.PEClientUtil;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.PEChangeSelectionMessage;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.Nullable;

public class EnchantersWorkbenchScreen extends AbstractContainerScreen<EnchantersWorkbenchMenu> {

  private static final ResourceLocation TEXTURE_LOCATION = PrecisionEnchanter.modLoc("textures/gui/enchanters_workbench.png");

  @Nullable
  private Component selectedEnchantmentText;
  private EnchantmentInstance currentEnchantment;
  private boolean matchesMultiple;

  private ChangeSelectionButton selectUpButton, selectDownButton;
  private final EnchantmentRecipeTabletComponent tablet;

  public EnchantersWorkbenchScreen(EnchantersWorkbenchMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    selectedEnchantmentText = null;
    matchesMultiple = false;
    currentEnchantment = null;
    tablet = new EnchantmentRecipeTabletComponent();
  }

  private void updateTabletFocus() {
    if (tablet.isVisible()) {
      setFocused(tablet);
    } else {
      setFocused(null);
    }
  }

  private void renderCost(GuiGraphics g, int cost, boolean includeCross) {
    if (cost > 0) {
      int color;
      if (EnchantmentRecipe.canCraftEnchantment(minecraft.player, cost)) {
        color = 0x38D600;
      } else {
        color = 0xFF3C3F;
        if (includeCross) {
          g.blit(TEXTURE_LOCATION, 104, 38, 176, 0, 17, 17);
        }
      }
      Component comp = ChatUtil.mut(cost);
      int xPos = 144 - (font.width(comp) / 2);
      g.blit(TEXTURE_LOCATION, xPos - 7, 63, 176, 17, 10, 10);
      g.drawString(font, comp, xPos + 7, 64, color);
    }
  }

  @Override
  protected void init() {
    super.init();
    selectUpButton = new ChangeSelectionButton(leftPos + 157, topPos + 34, 176, 27, 13, 13, PELang.SCREEN_SELECT_PREV, 1);
    selectDownButton = new ChangeSelectionButton(leftPos + 157, topPos + 47, 176, 40, 13, 13, PELang.SCREEN_SELECT_NEXT, -1);
    addRenderableWidget(selectUpButton);
    addRenderableWidget(selectDownButton);
    ImageButton showRecipesButton = new ImageButton(leftPos - 20, topPos + 20, 20, 20, 176, 53, TEXTURE_LOCATION, button -> {
      tablet.toggleVisibility();
      if (tablet.isVisible()) {
        leftPos = (width - 148) / 2 - 86 + 148;
        button.setPosition(leftPos - 168, topPos + 20);
        button.setTooltip(Tooltip.create(PELang.WORKBENCH_HIDE_RECIPES));
      } else {
        leftPos = (width - imageWidth) / 2;
        button.setPosition(leftPos - 20, topPos + 20);
        button.setTooltip(Tooltip.create(PELang.WORKBENCH_SHOW_RECIPES));
      }
      selectUpButton.setPosition(leftPos + 157, topPos + 34);
      selectDownButton.setPosition(leftPos + 157, topPos + 47);
      updateTabletFocus();
    });
    showRecipesButton.setTooltip(Tooltip.create(PELang.WORKBENCH_SHOW_RECIPES));
    addRenderableWidget(showRecipesButton);
    selectUpButton.visible = false;
    selectDownButton.visible = false;
    tablet.init(width, height, false, minecraft, menu);
    updateTabletFocus();
  }

  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    if (!menu.enchantmentsEquals(currentEnchantment) || (matchesMultiple != menu.multipleRecipesMatched())) {
      currentEnchantment = menu.getSelectedEnchantment();
      matchesMultiple = menu.multipleRecipesMatched();
      if (currentEnchantment == null) {
        selectedEnchantmentText = null;
      } else {
        selectedEnchantmentText = ChatUtil.mut(PEClientUtil.getEnchantmentFullName(currentEnchantment));
      }
      selectUpButton.visible = matchesMultiple;
      selectDownButton.visible = matchesMultiple;
    }

    renderBackground(g);
    super.render(g, mouseX, mouseY, partialTick);
    tablet.render(g, mouseX, mouseY, partialTick);
    renderTooltip(g, mouseX, mouseY);
    tablet.renderTooltips(g, mouseX, mouseY);
  }

  @Override
  protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
    g.blit(TEXTURE_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

  @Override
  protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
    super.renderLabels(g, mouseX, mouseY);
    if (selectedEnchantmentText != null) {
      g.drawCenteredString(font, selectedEnchantmentText, 88, 17, 0x38D600);
      renderCost(g, menu.getCost(), true);
    } else if (tablet.isVisible() && tablet.getPlaceholderRecipe().hasRecipe()) {
      renderCost(g, tablet.getPlaceholderRecipe().getRecipe().getCost(), false);
    }
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    tablet.tick();
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (tablet.mouseClicked(mouseX, mouseY, button)) {
      setFocused(tablet);
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  protected void slotClicked(Slot slot, int slotId, int button, ClickType clickType) {
    if (tablet.isVisible() && tablet.getPlaceholderRecipe().hasRecipe()) {
      if (slotId >= 2 && slotId <= 5) {
        tablet.getPlaceholderRecipe().clear();
      }
    }
    super.slotClicked(slot, slotId, button, clickType);
  }

  private class ChangeSelectionButton extends AbstractButton {

    final int srcX;
    final int srcY;
    final int amount;

    public ChangeSelectionButton(int x, int y, int srcX, int srcY, int width, int height, Component text, int amount) {
      super(x, y, width, height, text);
      this.srcX = srcX;
      this.srcY = srcY;
      this.amount = amount;
    }

    @Override
    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      int xOff = isHovered ? getWidth() : 0;
      g.blit(TEXTURE_LOCATION, getX(), getY(), srcX + xOff, srcY, getWidth(), getHeight());
    }

    @Override
    public void onPress() {
      PENetwork.sendToServer(new PEChangeSelectionMessage(menu.containerId, amount));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
      defaultButtonNarrationText(output);
    }

  }

}
