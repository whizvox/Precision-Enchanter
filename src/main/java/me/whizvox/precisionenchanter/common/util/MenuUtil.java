package me.whizvox.precisionenchanter.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class MenuUtil {

  public static boolean isValid(Player player, BlockPos pos) {
    // 8 blocks away
    return pos.distToCenterSqr(player.position()) <= 64.0;
  }

}
