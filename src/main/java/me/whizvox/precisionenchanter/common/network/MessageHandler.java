package me.whizvox.precisionenchanter.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface MessageHandler<T> {

  Class<T> getType();

  void encode(T msg, FriendlyByteBuf buf);

  T decode(FriendlyByteBuf buf);

  void handle(NetworkEvent.Context ctx, T msg);

}
