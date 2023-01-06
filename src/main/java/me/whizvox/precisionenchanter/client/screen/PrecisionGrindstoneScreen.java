package me.whizvox.precisionenchanter.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.PrecisionGrindstoneMenu;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.PEChangeSelectionMessage;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class PrecisionGrindstoneScreen extends AbstractContainerScreen<PrecisionGrindstoneMenu> {

  private static final ResourceLocation BG_LOCATION = PrecisionEnchanter.modLoc("textures/gui/precision_grindstone.png");

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
    prevButton = new ChangeSelectionButton(148, 35, 176, 0, 13, 13, PELang.SCREEN_SELECT_PREV, -1);
    nextButton = new ChangeSelectionButton(148, 48, 176, 13, 13, 13, PELang.SCREEN_SELECT_NEXT, 1);
    addRenderableWidget(prevButton);
    addRenderableWidget(nextButton);
    prevButton.visible = false;
    nextButton.visible = false;
  }

  @Override
  public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
    if (!menu.enchantmentEquals(selectedEnchantment)) {
      selectedEnchantment = menu.getSelectedEnchantment();
      selectedEnchantmentText = selectedEnchantment.enchantment.getFullname(selectedEnchantment.level);

      boolean flag = menu.hasMultipleEnchantments();
      prevButton.visible = flag;
      nextButton.visible = flag;
    }

    renderBackground(pose);
    super.render(pose, mouseX, mouseY, partialTick);
    renderTooltip(pose, mouseX, mouseY);
  }

  @Override
  protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, BG_LOCATION);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

  @Override
  protected void renderLabels(PoseStack pose, int mouseX, int mouseY) {
    super.renderLabels(pose, mouseX, mouseY);

    if (selectedEnchantmentText != null) {
      drawCenteredString(pose, font, selectedEnchantmentText, 88, 13, 0x38D600);
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
    public void renderButton(PoseStack pose, int mouseX, int mouseY, float partialTick) {
      int srcXOff = isHovered ? getWidth() : 0;
      RenderSystem.setShaderTexture(0, BG_LOCATION);
      blit(pose, getX(), getY(), srcX + srcXOff, srcY, getWidth(), getHeight());
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
