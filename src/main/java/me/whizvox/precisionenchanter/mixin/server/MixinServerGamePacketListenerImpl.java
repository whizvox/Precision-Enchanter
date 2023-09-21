package me.whizvox.precisionenchanter.mixin.server;

import me.whizvox.precisionenchanter.common.util.InventoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {

  @Shadow
  public ServerPlayer player;

  @Inject(method = "signBook",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addTagElement(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)V"),
      locals = LocalCapture.CAPTURE_FAILHARD)
  private void pe_signBook(FilteredText title, List<FilteredText> pages, int index, CallbackInfo ci,
                           ItemStack writableBook, ItemStack writtenBook) {
    InventoryUtil.setAuthorId(writtenBook, player);
  }

}
