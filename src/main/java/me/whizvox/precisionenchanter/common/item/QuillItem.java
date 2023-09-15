package me.whizvox.precisionenchanter.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class QuillItem extends Item {

  public QuillItem(Item.Properties props) {
    super(props);
  }

  public QuillItem() {
    this(new Item.Properties().durability(50));
  }

  @Override
  public InteractionResult useOn(UseOnContext ctx) {
    Level level = ctx.getLevel();
    Player player = ctx.getPlayer();
    ItemStack stack = ctx.getItemInHand();
    if ((!isDamageable(stack) || getDamage(stack) < getMaxDamage(stack)) && level.getBlockEntity(ctx.getClickedPos()) instanceof SignBlockEntity sign) {
      if (!level.isClientSide) {
        stack.hurt(1, player.getRandom(), (ServerPlayer) player);
        // TODO Allow editing of either side of sign
        player.openTextEdit(sign, true);
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
    }
    return InteractionResult.PASS;
  }

}
