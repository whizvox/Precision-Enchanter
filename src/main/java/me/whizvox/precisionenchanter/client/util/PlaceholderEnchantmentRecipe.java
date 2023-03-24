package me.whizvox.precisionenchanter.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PlaceholderEnchantmentRecipe {

  public int x, y;

  private Minecraft mc;
  @Nullable
  private EnchantmentRecipe recipe;
  private final List<PlaceholderIngredient> ingredients;
  private float time;

  public PlaceholderEnchantmentRecipe(int x, int y) {
    this.x = x;
    this.y = y;
    mc = null;
    recipe = null;
    ingredients = new ArrayList<>();
    time = 0;
  }

  public void init(Minecraft mc) {
    this.mc = mc;
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean hasRecipe() {
    return recipe != null;
  }

  @Nullable
  public EnchantmentRecipe getRecipe() {
    return recipe;
  }

  public void clear() {
    setRecipe(null);
    time = 0;
  }

  public void setRecipe(@Nullable EnchantmentRecipe recipe) {
    this.recipe = recipe;
    ingredients.clear();
    if (recipe != null) {
      recipe.getIngredients().forEach(pair -> {
        ItemStack[] stacks = pair.getLeft().getItems();
        for (ItemStack stack : stacks) {
          stack.setCount(pair.getRight());
        }
        int ix = x + (ingredients.size() % 2) * 18;
        int iy = y + (ingredients.size() / 2) * 18;
        ingredients.add(new PlaceholderIngredient(stacks, ix, iy));
      });
    }
  }

  public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
    if (hasRecipe()) {
      if (!Screen.hasControlDown()) {
        time += partialTick;
      }
      ingredients.forEach(ingredient -> {
        GuiComponent.fill(pose, ingredient.x, ingredient.y, ingredient.x + 16, ingredient.y + 16, 0x30FF0000);
        ingredient.render(mc, pose, time);
      });
    }
  }

  public void renderTooltip(PoseStack pose, int mouseX, int mouseY) {
    if (hasRecipe()) {
      ItemStack stack = null;
      for (PlaceholderIngredient ingredient : ingredients) {
        if (mouseX >= ingredient.x && mouseX < ingredient.x + 16 && mouseY >= ingredient.y && mouseY < ingredient.y + 16) {
          stack = ingredient.getItem(time);
          break;
        }
      }
      if (stack != null) {
        mc.screen.renderComponentTooltip(pose, mc.screen.getTooltipFromItem(stack), mouseX, mouseY, stack);
      }
    }
  }

  private record PlaceholderIngredient(ItemStack[] items, int x, int y) {

    public ItemStack getItem(float time) {
      return items[Mth.floor(time / 20) % items.length];
    }

    public void render(Minecraft mc, PoseStack pose, float time) {
      ItemStack stack = getItem(time);
      mc.getItemRenderer().renderAndDecorateFakeItem(pose, stack, x, y);
      RenderSystem.depthFunc(GL11.GL_GREATER);
      GuiComponent.fill(pose, x, y, x + 16, y + 16, 0x30FFFFFF);
      RenderSystem.depthFunc(GL11.GL_LEQUAL);
      mc.getItemRenderer().renderGuiItemDecorations(pose, mc.font, stack, x, y);
    }

  }

}
