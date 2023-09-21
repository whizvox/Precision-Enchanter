package me.whizvox.precisionenchanter.mixin.client;

import me.whizvox.precisionenchanter.common.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BookEditScreen.class)
public class MixinBookEditScreen {

  @Shadow @Final private ItemStack book;

  @Inject(method = "updateLocalCopy",
      at = @At("HEAD"),
      locals = LocalCapture.CAPTURE_FAILHARD)
  private void pe_updateLocalCopy(boolean sign, CallbackInfo ci) {
    InventoryUtil.setAuthorId(book, Minecraft.getInstance().player);
  }

}
