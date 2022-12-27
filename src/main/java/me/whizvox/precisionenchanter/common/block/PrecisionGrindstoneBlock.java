package me.whizvox.precisionenchanter.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PrecisionGrindstoneBlock extends Block {

  public PrecisionGrindstoneBlock() {
    super(BlockBehaviour.Properties.of(Material.STONE).strength(2.5F, 7.0F));
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      // open gui
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Nullable
  @Override
  public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    return super.getMenuProvider(state, level, pos);
  }

}
