package me.whizvox.precisionenchanter.common.block;

import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class EnchantersWorkbenchBlock extends Block {

  private static final VoxelShape SHAPE = Shapes.or(
      Block.box(0, 0, 0, 3, 12, 3),
      Block.box(0, 0, 13, 3, 12, 16),
      Block.box(13, 0, 0, 16, 12, 3),
      Block.box(13, 0, 13, 16, 12, 16),
      Block.box(0, 12, 0, 16, 15, 16),
      Block.box(3, 8, 0, 13, 12, 0.1),
      Block.box(3, 8, 15.9, 13, 12, 16),
      Block.box(0, 8, 3, 0.1, 12, 13),
      Block.box(15.9, 8, 3, 16, 12, 13)
  );

  public EnchantersWorkbenchBlock() {
    super(BlockBehaviour.Properties.of(Material.STONE).strength(2.5F, 7.0F));
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState state) {
    return true;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    return SHAPE;
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      NetworkHooks.openScreen(serverPlayer, state.getMenuProvider(level, pos), pos);
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Nullable
  @Override
  public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    return new SimpleMenuProvider(
        (containerId, playerInv, player) -> new EnchantersWorkbenchMenu(containerId, playerInv, ContainerLevelAccess.create(level, pos)),
        PELang.CONTAINER_ENCHANTERS_WORKBENCH
    );
  }

}
