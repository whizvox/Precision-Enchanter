package me.whizvox.precisionenchanter.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.client.util.PEClientUtil;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.PEChangeSelectionMessage;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.util.ChatUtil;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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

  private ChangeSelectionButton selectUpButton, selectDownButton;
  private final EnchantmentRecipeTabletComponent tablet;

  public EnchantersWorkbenchScreen(EnchantersWorkbenchMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    selectedEnchantmentText = null;
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

  private void renderCost(PoseStack pose, int cost, boolean includeCross) {
    if (cost > 0) {
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      int color;
      if (EnchantmentRecipe.canCraftEnchantment(minecraft.player, cost)) {
        color = 0x38D600;
      } else {
        color = 0xFF3C3F;
        if (includeCross) {
          blit(pose, 104, 38, 176, 0, 17, 17);
        }
      }
      Component comp = ChatUtil.mut(cost);
      int xPos = 144 - (font.width(comp) / 2);
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      blit(pose, xPos - 7, 63, 176, 17, 10, 10);
      drawString(pose, font, comp, xPos + 7, 64, color);
    }
  }

  @Override
  protected void init() {
    super.init();
    selectUpButton = new ChangeSelectionButton(leftPos + 157, topPos + 34, 176, 27, 13, 13, PELang.SCREEN_SELECT_PREV, 1);
    selectDownButton = new ChangeSelectionButton(leftPos + 157, topPos + 47, 176, 40, 13, 13, PELang.SCREEN_SELECT_NEXT, -1);
    addRenderableWidget(selectUpButton);
    addRenderableWidget(selectDownButton);

    addRenderableWidget(new ImageButton(leftPos - 20, topPos + 20, 20, 20, 176, 53, 20, TEXTURE_LOCATION, 256, 256, button -> {
      tablet.toggleVisibility();
      if (tablet.isVisible()) {
        leftPos = (width - 148) / 2 - 86 + 148;
        button.x = leftPos - 168;
      } else {
        leftPos = (width - imageWidth) / 2;
        button.x = leftPos - 20;
      }
      button.y = topPos + 20;
      selectUpButton.x = leftPos + 157;
      selectUpButton.y = topPos + 34;
      selectDownButton.x = selectUpButton.x;
      selectDownButton.y = topPos + 47;
      updateTabletFocus();
    }, (button, pose, mouseX, mouseY) -> {
      renderTooltip(pose, tablet.isVisible() ? PELang.WORKBENCH_HIDE_RECIPES : PELang.WORKBENCH_SHOW_RECIPES, mouseX, mouseY);
    }, PELang.WORKBENCH_SHOW_RECIPES));

    selectUpButton.visible = false;
    selectDownButton.visible = false;
    tablet.init(width, height, false, minecraft, menu);
    updateTabletFocus();
  }

  @Override
  public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
    if (!menu.enchantmentsEquals(currentEnchantment)) {
      currentEnchantment = menu.getSelectedEnchantment();
      if (currentEnchantment == null) {
        selectedEnchantmentText = null;
      } else {
        selectedEnchantmentText = ChatUtil.mut(PEClientUtil.getEnchantmentFullName(currentEnchantment));
      }
      boolean flag = menu.multipleRecipesMatched();
      selectUpButton.visible = flag;
      selectDownButton.visible = flag;
    }

    renderBackground(pose);
    super.render(pose, mouseX, mouseY, partialTick);
    tablet.render(pose, mouseX, mouseY, partialTick);
    renderTooltip(pose, mouseX, mouseY);
    tablet.renderTooltips(pose, mouseX, mouseY);
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
      renderCost(pose, menu.getCost(), true);
    } else if (tablet.isVisible() && tablet.getPlaceholderRecipe().hasRecipe()) {
      renderCost(pose, tablet.getPlaceholderRecipe().getRecipe().getCost(), false);
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
    public void renderButton(PoseStack pose, int mouseX, int mouseY, float partialTick) {
      int xOff = isHovered ? getWidth() : 0;
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      blit(pose, x, y, srcX + xOff, srcY, getWidth(), getHeight());
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
