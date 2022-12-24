package me.whizvox.precisionenchanter.common.network.message;

import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.network.MessageHandler;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SimpleClientBoundMessage(SimpleClientBoundMessage.Action action) {

  public enum Action {
    ENCHANTMENT_RECIPES_RELOADED
  }

  public static SimpleClientBoundMessage
      ENCHANTMENT_RECIPES_RELOADED = new SimpleClientBoundMessage(Action.ENCHANTMENT_RECIPES_RELOADED);

  public static final MessageHandler<SimpleClientBoundMessage> HANDLER = new MessageHandler<>() {

    @Override
    public Class<SimpleClientBoundMessage> getType() {
      return SimpleClientBoundMessage.class;
    }

    @Override
    public void encode(SimpleClientBoundMessage msg, FriendlyByteBuf buf) {
      buf.writeEnum(msg.action);
    }

    @Override
    public SimpleClientBoundMessage decode(FriendlyByteBuf buf) {
      return new SimpleClientBoundMessage(buf.readEnum(Action.class));
    }

    @Override
    public void handle(NetworkEvent.Context ctx, SimpleClientBoundMessage msg) {
      //noinspection SwitchStatementWithTooFewBranches
      switch (msg.action) {
        case ENCHANTMENT_RECIPES_RELOADED -> {
          EnchantmentRecipeManager.INSTANCE.clear();
        }
        default -> PELog.LOGGER.warn(PELog.M_CLIENT, "Unknown generic broadcast action: {}", msg.action);
      }
    }

  };

}
