package me.whizvox.precisionenchanter.common.network.message;

import me.whizvox.precisionenchanter.common.network.MessageHandler;
import me.whizvox.precisionenchanter.common.util.PEEnchantmentHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record SyncEnchantmentsMessage(List<Enchantment> enchantments) {

  public static final MessageHandler<SyncEnchantmentsMessage> HANDLER = new MessageHandler<>() {

    @Override
    public Class<SyncEnchantmentsMessage> getType() {
      return SyncEnchantmentsMessage.class;
    }

    @Override
    public void encode(SyncEnchantmentsMessage msg, FriendlyByteBuf buf) {
      buf.writeShort(msg.enchantments.size());
      //noinspection ConstantConditions
      msg.enchantments.forEach(enchantment -> buf.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)));
    }

    @Override
    public SyncEnchantmentsMessage decode(FriendlyByteBuf buf) {
      int numEnchantments = buf.readShort();
      List<Enchantment> enchantments = new ArrayList<>(numEnchantments);
      for (int i = 0; i < numEnchantments; i++) {
        enchantments.add(ForgeRegistries.ENCHANTMENTS.getValue(buf.readResourceLocation()));
      }
      return new SyncEnchantmentsMessage(Collections.unmodifiableList(enchantments));
    }

    @Override
    public void handle(NetworkEvent.Context ctx, SyncEnchantmentsMessage msg) {
      PEEnchantmentHelper.INSTANCE.sync(msg.enchantments);
    }

  };

}
