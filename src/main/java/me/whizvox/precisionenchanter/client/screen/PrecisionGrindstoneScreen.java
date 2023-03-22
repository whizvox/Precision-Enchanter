package me.whizvox.precisionenchanter.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.precisionenchanter.client.util.PEClientUtil;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.PrecisionGrindstoneMenu;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.PEChangeSelectionMessage;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
  public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
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

    renderBackground(pose);
    super.render(pose, mouseX, mouseY, partialTick);
    renderTooltip(pose, mouseX, mouseY);
  }

  @Override
  protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

  @Override
  protected void renderLabels(PoseStack pose, int mouseX, int mouseY) {
    super.renderLabels(pose, mouseX, mouseY);

    if (selectedEnchantmentText != null) {
      drawCenteredString(pose, font, selectedEnchantmentText, 88, 17, 0x38D600);
    }
    int cost = menu.getCost();
    if (cost > 0) {
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      int color;
      if (EnchantmentRecipe.canCraftEnchantment(minecraft.player, cost)) {
        color = 0x38D600;
      } else {
        color = 0xFF3C3F;
        blit(pose, 95, 38, 176, 0, 17, 17);
      }
      Component comp = ChatUtil.mut(cost);
      int xPos = 135 - (font.width(comp) / 2);
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      blit(pose, xPos - 7, 63, 176, 17, 10, 10);
      drawString(pose, font, comp, xPos + 7, 64, color);
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
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      blit(pose, x, y, srcX + srcXOff, srcY, getWidth(), getHeight());
    }

    @Override
    public void onPress() {
      PENetwork.sendToServer(new PEChangeSelectionMessage(menu.containerId, amount));
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
      defaultButtonNarrationText(output);
    }

  }

}
