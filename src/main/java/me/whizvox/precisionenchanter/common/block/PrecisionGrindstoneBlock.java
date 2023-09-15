package me.whizvox.precisionenchanter.common.block;

import me.whizvox.precisionenchanter.common.lib.PELang;
import me.whizvox.precisionenchanter.common.menu.PrecisionGrindstoneMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class PrecisionGrindstoneBlock extends Block {

  private static final VoxelShape SHAPE_NS = Shapes.or(
      Block.box(3, 0, 0, 5, 2, 16),
      Block.box(11, 0, 0, 13, 2, 16),
      Block.box(5, 0, 0, 11, 2, 2),
      Block.box(5, 0, 14, 11, 2, 16),
      Block.box(3, 2, 6, 5, 7, 10),
      Block.box(11, 2, 6, 13, 7, 10),
      Block.box(3, 7, 5, 7, 13, 11),
      Block.box(11, 7, 5, 13, 13, 11),
      Block.box(5, 4, 2, 11, 16, 14)
  );
  private static final VoxelShape SHAPE_EW = Shapes.or(
      Block.box(0, 0, 3, 16, 2, 5),
      Block.box(0, 0, 11, 16, 2, 13),
      Block.box(0, 0, 5, 2, 2, 11),
      Block.box(14, 0, 5, 16, 2, 11),
      Block.box(6, 2, 3, 10, 7, 5),
      Block.box(6, 2, 11, 10, 7, 13),
      Block.box(5, 7, 3, 11, 13, 7),
      Block.box(5, 7, 11, 11, 13, 13),
      Block.box(2, 4, 5, 14, 16, 11)
  );

  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

  public PrecisionGrindstoneBlock() {
    super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).strength(2.5F, 7.0F));
    registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
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
    return switch (state.getValue(FACING)) {
      case EAST, WEST -> SHAPE_EW;
      default -> SHAPE_NS;
    };
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
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
        (containerId, playerInv, player) -> new PrecisionGrindstoneMenu(containerId, playerInv, ContainerLevelAccess.create(level, pos)),
        PELang.CONTAINER_PRECISION_GRINDSTONE
    );
  }

}
