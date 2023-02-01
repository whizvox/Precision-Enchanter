package me.whizvox.precisionenchanter.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.SimpleServerBoundMessage;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class EnchantmentRecipeTabletComponent extends GuiComponent implements Renderable, GuiEventListener, NarratableEntry {

  private static final ResourceLocation TEXTURE_LOCATION = PrecisionEnchanter.modLoc("textures/gui/enchantment_recipe_tablet.png");
  private static final int
      SYNC_TIMEOUT = 5, // in seconds
      MAX_SYNC_ATTEMPTS = 10,
      MAX_RECIPES_PER_PAGE = 7;

  private int leftPos, topPos;
  private Minecraft mc;
  private int width, height;
  private boolean visible;
  private boolean recipesLoaded;
  private EnchantersWorkbenchMenu menu;

  // Time when last sync attempt occurred
  private LocalDateTime attemptRecipesSyncTime;
  // Number of recipe sync attempts
  private byte syncAttempts;
  // Whether the maximum number of recipe sync attempts has been reached
  private boolean syncFailed;
  private List<EnchantmentRecipeInfo> filteredRecipes;
  private List<EnchantmentEntry> displayedEntries;
  private int currentRecipePage;
  private EditBox searchBar;
  private String lastSearch;

  private Component pageNumberComponent;
  private ImageButton prevPageButton, nextPageButton;

  public void init(int width, int height, boolean visible, Minecraft mc, EnchantersWorkbenchMenu menu) {
    this.width = width;
    this.height = height;
    this.menu = menu;
    this.mc = mc;
    this.visible = visible;
    recipesLoaded = false;
    filteredRecipes = new ArrayList<>();
    displayedEntries = new ArrayList<>();
    if (visible) {
      initVisuals();
    }
  }

  private void initVisuals() {
    if (!recipesLoaded) {
      if (EnchantmentRecipeManager.INSTANCE.isInitialized()) {
        recipesLoaded = true;
      } else {
        PENetwork.sendToServer(SimpleServerBoundMessage.REQUEST_SYNC_ENCHANTMENT_RECIPES);
        attemptRecipesSyncTime = LocalDateTime.now();
        syncAttempts = 0;
        syncFailed = false;
      }
    }
    leftPos = (width - 148) / 2 - 86;
    topPos = (height - 167) / 2;
    currentRecipePage = 0;
    lastSearch = "";
    searchBar = new EditBox(mc.font, leftPos + 18, topPos + 6, 123, 13, Component.translatable("itemGroup.search"));
    searchBar.setMaxLength(50);
    searchBar.setBordered(false);
    searchBar.setVisible(true);
    searchBar.setTextColor(0xFFFFFF);
    searchBar.setHint(PELang.TABLET_SEARCH_HINT);
    searchBar.active = recipesLoaded;
    prevPageButton = new ImageButton(leftPos + 39, topPos + 151, 12, 12, 160, 0, TEXTURE_LOCATION, button -> {
      if (currentRecipePage > 0) {
        currentRecipePage--;
        updateEnchantmentEntries();
      }
    });
    nextPageButton = new ImageButton(leftPos + 97, topPos + 151, 12, 12, 172, 0, TEXTURE_LOCATION, button -> {
      if ((currentRecipePage + 1) * MAX_RECIPES_PER_PAGE < filteredRecipes.size()) {
        currentRecipePage++;
        updateEnchantmentEntries();
      }
    });
    updateEnchantmentEntries();
  }

  private void updateEnchantmentEntries() {
    String search = searchBar.getValue();
    if (!search.equalsIgnoreCase(lastSearch)) {
      currentRecipePage = 0;
      lastSearch = search;
    }
    filteredRecipes.clear();
    displayedEntries.clear();
    String filter = search.toLowerCase(Locale.getDefault());
    Stream<EnchantmentRecipeInfo> stream = EnchantmentRecipeManager.INSTANCE.stream()
        .map(entry -> new EnchantmentRecipeInfo(entry.getKey(), entry.getValue(), entry.getValue().getEnchantment().getFullname(entry.getValue().getLevel()).getString()));
    if (!filter.isEmpty()) {
      stream = stream.filter(info -> info.translatedString.toLowerCase(Locale.getDefault()).contains(filter));
    }
    stream.sorted(Comparator.comparing(o -> o.translatedString))
        .forEach(info -> filteredRecipes.add(info));
    for (int i = currentRecipePage * MAX_RECIPES_PER_PAGE; i < ((currentRecipePage + 1) * MAX_RECIPES_PER_PAGE) && i < filteredRecipes.size(); i++) {
      displayedEntries.add(new EnchantmentEntry(17 + (i % MAX_RECIPES_PER_PAGE) * 19, filteredRecipes.get(i)));
    }
    if (displayedEntries.isEmpty()) {
      prevPageButton.visible = false;
      nextPageButton.visible = false;
      pageNumberComponent = Component.literal("0 / 0");
    } else {
      prevPageButton.visible = currentRecipePage > 0;
      nextPageButton.visible = (currentRecipePage + 1) * MAX_RECIPES_PER_PAGE < filteredRecipes.size();
      pageNumberComponent = Component.literal((currentRecipePage + 1) + " / " + ((filteredRecipes.size() - 1) / MAX_RECIPES_PER_PAGE + 1));
    }
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    if (visible) {
      initVisuals();
    }
    this.visible = visible;
  }

  public void toggleVisibility() {
    setVisible(!visible);
  }

  public void tick() {
    if (visible) {
      if (!recipesLoaded) {
        if (EnchantmentRecipeManager.INSTANCE.isInitialized()) {
          recipesLoaded = true;
          updateEnchantmentEntries();
        } else if (!syncFailed) {
          LocalDateTime now = LocalDateTime.now();
          if (Duration.between(attemptRecipesSyncTime, now).toSeconds() > SYNC_TIMEOUT) {
            syncAttempts++;
            if (syncAttempts > MAX_SYNC_ATTEMPTS) {
              syncFailed = true;
              PELog.LOGGER.warn(PELog.M_CLIENT, "Failed to sync enchantment recipes with server");
            } else {
              PENetwork.sendToServer(SimpleServerBoundMessage.REQUEST_SYNC_ENCHANTMENT_RECIPES);
              attemptRecipesSyncTime = LocalDateTime.now();
            }
          }
        }
      }
      searchBar.tick();
    }
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (visible && searchBar.charTyped(codePoint, modifiers)) {
      updateEnchantmentEntries();
      return true;
    }
    return false;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (visible) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
        if (searchBar.isFocused()) {
          searchBar.setFocus(false);
        } else {
          setVisible(false);
        }
        return true;
      }
      if (mc.options.keyChat.matches(keyCode, scanCode) && !searchBar.isFocused()) {
        searchBar.setFocus(true);
        return true;
      }
      if (searchBar.isFocused()) {
        searchBar.keyPressed(keyCode, scanCode, modifiers);
        updateEnchantmentEntries();
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (visible) {
      if (searchBar.mouseClicked(mouseX, mouseY, button)) {
        return true;
      }
      if (prevPageButton.mouseClicked(mouseX, mouseY, button)) {
        return true;
      }
      if (nextPageButton.mouseClicked(mouseX, mouseY, button)) {
        return true;
      }
      for (EnchantmentEntry entry : displayedEntries) {
        if (entry.mouseClicked(mouseX, mouseY, button)) {
          // TODO Display ghost ingredients in input slots
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
    if (visible) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      RenderSystem.setShaderColor(1, 1, 1, 1);
      blit(pose, leftPos, topPos, 0, 0, 148, 167);
      blit(pose, leftPos + 4, topPos + 4, 148, 0, 12, 12);
      if (!recipesLoaded) {
        drawCenteredString(pose, mc.font, PELang.SCREEN_LOADING, leftPos + 74, topPos + 90, 0xFFFFFF);
      }
      drawCenteredString(pose, mc.font, pageNumberComponent, leftPos + 74, topPos + 153, 0xFFFFFF);
      searchBar.render(pose, mouseX, mouseY, partialTick);
      prevPageButton.render(pose, mouseX, mouseY, partialTick);
      nextPageButton.render(pose, mouseX, mouseY, partialTick);
      displayedEntries.forEach(entry -> entry.render(pose, mouseX, mouseY, partialTick));
    }
  }

  @Override
  public NarrationPriority narrationPriority() {
    return visible ? NarrationPriority.HOVERED : NarrationPriority.NONE;
  }

  @Override
  public void updateNarration(NarrationElementOutput output) {
    List<NarratableEntry> entries = new ArrayList<>();
    entries.add(searchBar);
    entries.addAll(displayedEntries);
    Screen.NarratableSearchResult result = Screen.findNarratableWidget(entries, null);
    if (result != null) {
      result.entry.updateNarration(output);
    }
  }

  private record EnchantmentRecipeInfo(ResourceLocation id, EnchantmentRecipe recipe, String translatedString) {}

  private class EnchantmentEntry extends AbstractButton {
    final EnchantmentRecipeInfo info;
    public EnchantmentEntry(int y, EnchantmentRecipeInfo info) {
      super(leftPos + 3, topPos + y, 142, 19, Component.literal(info.translatedString));
      this.info = info;
    }
    @Override
    public void renderButton(PoseStack pose, int mouseX, int mouseY, float partialTick) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
      RenderSystem.setShaderColor(1, 1, 1, 1);
      int srcY = isHovered ? 186 : 167;
      blit(pose, getX(), getY(), 0, srcY, width, height);
      //drawString(pose, mc.font, getMessage(), getX() + 5, getY() + 6, 0x000000);
      mc.font.draw(pose, getMessage(), getX() + 5, getY() + 6, 0x000000);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
      defaultButtonNarrationText(output);
    }
    @Override
    public void onPress() {
    }
  }

}
