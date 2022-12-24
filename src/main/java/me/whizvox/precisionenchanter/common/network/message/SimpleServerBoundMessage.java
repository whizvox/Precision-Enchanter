package me.whizvox.precisionenchanter.common.network.message;

import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.network.MessageHandler;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SimpleServerBoundMessage(Action action) {

  public enum Action {
    REQUEST_SYNC_ENCHANTMENT_RECIPES
  }

  public static final SimpleServerBoundMessage
      REQUEST_SYNC_ENCHANTMENT_RECIPES = new SimpleServerBoundMessage(Action.REQUEST_SYNC_ENCHANTMENT_RECIPES);

  public static final MessageHandler<SimpleServerBoundMessage> HANDLER = new MessageHandler<>() {

    @Override
    public Class<SimpleServerBoundMessage> getType() {
      return SimpleServerBoundMessage.class;
    }

    @Override
    public void encode(SimpleServerBoundMessage msg, FriendlyByteBuf buf) {
      buf.writeEnum(msg.action);
    }

    @Override
    public SimpleServerBoundMessage decode(FriendlyByteBuf buf) {
      return new SimpleServerBoundMessage(buf.readEnum(Action.class));
    }

    @Override
    public void handle(NetworkEvent.Context ctx, SimpleServerBoundMessage msg) {
      //noinspection SwitchStatementWithTooFewBranches
      switch (msg.action) {
        case REQUEST_SYNC_ENCHANTMENT_RECIPES -> {
          if (ctx.getSender() == null) {
            PELog.LOGGER.warn(PELog.M_SERVER, "Attempted to send enchantment recipe sync data to non-player");
          } else {
            PENetwork.sendToClient(EnchantmentRecipeManager.INSTANCE.createSyncMessage(), ctx.getSender());
          }
        }
        default -> PELog.LOGGER.warn(PELog.M_SERVER, "Unknown action: {}", msg.action);
      }
    }

  };

}
