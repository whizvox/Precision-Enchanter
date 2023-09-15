package me.whizvox.precisionenchanter.common.network;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.network.message.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PENetwork {

  public static final String PROTOCOL_VERSION = "1";

  private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
      PrecisionEnchanter.modLoc("main"),
      () -> PROTOCOL_VERSION,
      PROTOCOL_VERSION::equals,
      PROTOCOL_VERSION::equals
  );

  private static int lastId = 0;

  private static <T> void register(MessageHandler<T> handler, @Nullable NetworkDirection direction) {
    CHANNEL.registerMessage(lastId++, handler.getType(), handler::encode, handler::decode, (msg, ctxSupplier) -> {
      NetworkEvent.Context ctx = ctxSupplier.get();
      ctx.enqueueWork(() -> handler.handle(ctx, msg));
      ctx.setPacketHandled(true);
    }, Optional.ofNullable(direction));
  }

  private static <T> void register(MessageHandler<T> handler) {
    register(handler, null);
  }

  public static void register() {
    register(SimpleClientBoundMessage.HANDLER, NetworkDirection.PLAY_TO_CLIENT);
    register(SimpleServerBoundMessage.HANDLER, NetworkDirection.PLAY_TO_SERVER);
    register(SyncEnchantmentRecipesMessage.HANDLER, NetworkDirection.PLAY_TO_CLIENT);
    register(PEChangeSelectionMessage.HANDLER, NetworkDirection.PLAY_TO_SERVER);
    register(SyncEnchantmentsMessage.HANDLER, NetworkDirection.PLAY_TO_CLIENT);
    register(MatchThenMoveEnchantmentRecipeIngredientsMessage.HANDLER, NetworkDirection.PLAY_TO_SERVER);
  }

  public static void sendToServer(Object msg) {
    CHANNEL.sendToServer(msg);
  }

  public static void sendToClient(Object msg, ServerPlayer player) {
    CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
  }

  public static void broadcast(Object msg) {
    CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
  }

}
