package me.whizvox.precisionenchanter.common.network.message;

import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import me.whizvox.precisionenchanter.common.menu.PrecisionGrindstoneMenu;
import me.whizvox.precisionenchanter.common.network.MessageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

public record PEChangeSelectionMessage(int containerId, int amount) {

  public static final MessageHandler<PEChangeSelectionMessage> HANDLER = new MessageHandler<>() {

    @Override
    public Class<PEChangeSelectionMessage> getType() {
      return PEChangeSelectionMessage.class;
    }

    @Override
    public void encode(PEChangeSelectionMessage msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.containerId);
      buf.writeByte(msg.amount);
    }

    @Override
    public PEChangeSelectionMessage decode(FriendlyByteBuf buf) {
      return new PEChangeSelectionMessage(buf.readInt(), buf.readByte());
    }

    @Override
    public void handle(NetworkEvent.Context ctx, PEChangeSelectionMessage msg) {
      ServerPlayer player = ctx.getSender();
      if (player == null) {
        PELog.LOGGER.warn(PELog.M_SERVER, "Attempted to update enchanter's workbench selection for a non-player");
      } else {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu.containerId != msg.containerId) {
          PELog.LOGGER.warn(PELog.M_SERVER, "Open container for ({}) does not match enchanter's workbench selection request: expected={}, actual={}", player, msg.containerId, menu.containerId);
        } else {
          if (menu instanceof EnchantersWorkbenchMenu workbench) {
            workbench.changeSelection(msg.amount);
          } else if (menu instanceof PrecisionGrindstoneMenu grindstone) {
            grindstone.changeSelection(msg.amount);
          } else {
            PELog.LOGGER.warn("Open container for {} is not an enchanter's workbench: actual={}", player, menu.getClass());
          }
        }
      }
    }

  };

}
