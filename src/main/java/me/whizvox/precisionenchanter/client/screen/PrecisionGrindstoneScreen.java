package me.whizvox.precisionenchanter.client.screen;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.client.util.PEClientUtil;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.PrecisionGrindstoneMenu;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.PEChangeSelectionMessage;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class PrecisionGrindstoneScreen extends AbstractContainerScreen<PrecisionGrindstoneMenu> {

  private static final ResourceLocation TEXTURE_LOCATION = PrecisionEnchanter.modLoc("textures/gui/precision_grindstone.png");

  private EnchantmentInstance selectedEnchantment;
  private Component selectedEnchantmentText;

  private ChangeSelectionButton prevButton;
  private ChangeSelectionButton nextButton;

  public PrecisionGrindstoneScreen(PrecisionGrindstoneMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
    super(pMenu, pPlayerInventory, pTitle);
    selectedEnchantment = null;
    selectedEnchantmentText = null;
  }

  @Override
  protected void init() {
    super.init();
    prevButton = new ChangeSelectionButton(leftPos + 148, topPos + 34, 176, 27, 13, 13, PELang.SCREEN_SELECT_PREV, -1);
    nextButton = new ChangeSelectionButton(leftPos + 148, topPos + 47, 176, 40, 13, 13, PELang.SCREEN_SELECT_NEXT, 1);
    addRenderableWidget(prevButton);
    addRenderableWidget(nextButton);
    prevButton.visible = false;
    nextButton.visible = false;
  }

  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    if (!menu.enchantmentEquals(selectedEnchantment)) {
      selectedEnchantment = menu.getSelectedEnchantment();
      if (selectedEnchantment == null) {
        selectedEnchantmentText = null;
      } else {
        selectedEnchantmentText = ChatUtil.mut(PEClientUtil.getEnchantmentFullName(selectedEnchantment));
      }

      boolean flag = menu.hasMultipleEnchantments();
      prevButton.visible = flag;
      nextButton.visible = flag;
    }

    renderBackground(g);
    super.render(g, mouseX, mouseY, partialTick);
    renderTooltip(g, mouseX, mouseY);
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
    }
    int cost = menu.getCost();
    if (cost > 0) {
      int color;
      if (EnchantmentRecipe.canCraftEnchantment(minecraft.player, cost)) {
        color = 0x38D600;
      } else {
        color = 0xFF3C3F;
        g.blit(TEXTURE_LOCATION, 95, 38, 176, 0, 17, 17);
      }
      Component comp = ChatUtil.mut(cost);
      int xPos = 135 - (font.width(comp) / 2);
      g.blit(TEXTURE_LOCATION, xPos - 7, 63, 176, 17, 10, 10);
      g.drawString(font, comp, xPos + 7, 64, color);
    }
  }

  private class ChangeSelectionButton extends AbstractButton {

    private final int srcX;
    private final int srcY;
    private final int amount;

    public ChangeSelectionButton(int x, int y, int srcX, int srcY, int width, int height, Component msg, int amount) {
      super(x, y, width, height, msg);
      this.srcX = srcX;
      this.srcY = srcY;
      this.amount = amount;
    }

    @Override
    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      int srcXOff = isHovered ? getWidth() : 0;
      g.blit(TEXTURE_LOCATION, getX(), getY(), srcX + srcXOff, srcY, getWidth(), getHeight());
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
