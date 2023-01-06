package me.whizvox.precisionenchanter.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
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
import org.jetbrains.annotations.Nullable;

public class EnchantersWorkbenchScreen extends AbstractContainerScreen<EnchantersWorkbenchMenu> {

  private static final ResourceLocation TEXTURE_LOCATION = PrecisionEnchanter.modLoc("textures/gui/enchanters_workbench.png");

  @Nullable
  private Component selectedEnchantmentText;
  @Nullable
  private Component costText;
  private int costTextXPos;
  private EnchantmentInstance currentEnchantment;

  private ChangeSelectionButton selectUpButton, selectDownButton;

  public EnchantersWorkbenchScreen(EnchantersWorkbenchMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    selectedEnchantmentText = null;
    costText = null;
    costTextXPos = 0;
    currentEnchantment = null;
  }

  @Override
  protected void init() {
    super.init();
    selectUpButton = new ChangeSelectionButton(leftPos + 157, topPos + 34, 176, 27, 13, 13, PELang.SCREEN_SELECT_PREV, 1);
    selectDownButton = new ChangeSelectionButton(leftPos + 157, topPos + 47, 176, 40, 13, 13, PELang.SCREEN_SELECT_NEXT, -1);
    addRenderableWidget(selectUpButton);
    addRenderableWidget(selectDownButton);
    selectUpButton.visible = false;
    selectDownButton.visible = false;
  }

  @Override
  public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
    if (!menu.enchantmentsEquals(currentEnchantment)) {
      currentEnchantment = menu.getSelectedEnchantment();
      if (currentEnchantment == null) {
        selectedEnchantmentText = null;
        costText = null;
      } else {
        int cost = menu.getCost();
        selectedEnchantmentText = ChatUtil.reset(currentEnchantment.enchantment.getFullname(currentEnchantment.level));
        costText = Component.literal(String.valueOf(cost));
        costTextXPos = 144 - (font.width(costText) / 2);
      }
      boolean flag = menu.multipleRecipesMatched();
      selectUpButton.visible = flag;
      selectDownButton.visible = flag;
    }

    renderBackground(pose);
    super.render(pose, mouseX, mouseY, partialTick);
    renderTooltip(pose, mouseX, mouseY);
  }

  @Override
  protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
    blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

  @Override
  protected void renderLabels(PoseStack pose, int mouseX, int mouseY) {
    super.renderLabels(pose, mouseX, mouseY);
    if (selectedEnchantmentText != null) {
      drawCenteredString(pose, font, selectedEnchantmentText, 88, 17, 0x38D600);
    }
    if (costText != null) {
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      int color;
      if (EnchantmentRecipe.canCraftEnchantment(minecraft.player, menu.getCost())) {
        color = 0x38D600;
      } else {
        color = 0xFF3C3F;
        blit(pose, 104, 38, 176, 0, 17, 17);
      }
      blit(pose, costTextXPos - 7, 63, 176, 17, 10, 10);
      drawString(pose, font, costText, costTextXPos + 7, 64, color);
    }
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
    public void renderButton(PoseStack pose, int mouseX, int mouseY, float partialTick) {
      int xOff = isHovered ? getWidth() : 0;
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      blit(pose, getX(), getY(), srcX + xOff, srcY, getWidth(), getHeight());
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
