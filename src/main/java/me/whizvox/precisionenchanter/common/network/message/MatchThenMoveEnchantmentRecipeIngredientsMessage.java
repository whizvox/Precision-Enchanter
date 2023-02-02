package me.whizvox.precisionenchanter.common.network.message;

import me.whizvox.precisionenchanter.common.lib.PELog;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import me.whizvox.precisionenchanter.common.network.MessageHandler;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public record MatchThenMoveEnchantmentRecipeIngredientsMessage(EnchantmentRecipe recipe) {

  public static final MessageHandler<MatchThenMoveEnchantmentRecipeIngredientsMessage> HANDLER = new MessageHandler<>() {

    @Override
    public Class<MatchThenMoveEnchantmentRecipeIngredientsMessage> getType() {
      return MatchThenMoveEnchantmentRecipeIngredientsMessage.class;
    }

    @Override
    public void encode(MatchThenMoveEnchantmentRecipeIngredientsMessage msg, FriendlyByteBuf buf) {
      buf.writeResourceLocation(msg.recipe.getId());
    }

    @Override
    public MatchThenMoveEnchantmentRecipeIngredientsMessage decode(FriendlyByteBuf buf) {
      ResourceLocation id = buf.readResourceLocation();
      EnchantmentRecipe recipe = EnchantmentRecipeManager.INSTANCE.get(id);
      if (recipe == null) {
        PELog.LOGGER.warn(PELog.M_SERVER, "Unknown enchantment recipe: {}", id);
      }
      return new MatchThenMoveEnchantmentRecipeIngredientsMessage(recipe);
    }

    @Override
    public void handle(NetworkEvent.Context ctx, MatchThenMoveEnchantmentRecipeIngredientsMessage msg) {
      if (msg.recipe != null) {
        ServerPlayer sender = ctx.getSender();
        if (sender == null) {
          PELog.LOGGER.warn(PELog.M_SERVER, "Attempted to handle message {} with non-player", getType());
        } else {
          if (sender.containerMenu instanceof EnchantersWorkbenchMenu workbench) {
            if (!workbench.matchThenMove(sender, msg.recipe)) {
              //PELog.LOGGER.warn(PELog.M_SERVER, "Could not move ");
            }
          }
        }
      }
    }

  };

}
