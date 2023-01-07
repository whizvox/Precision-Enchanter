package me.whizvox.precisionenchanter.data.client;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.block.PrecisionGrindstoneBlock;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PEBlockStateProvider extends BlockStateProvider {

  public PEBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, PrecisionEnchanter.MOD_ID, existingFileHelper);
  }

  void registerEnchantersWorkbench() {
    ModelFile model = models().getExistingFile(modLoc("block/enchanters_workbench"));
    getVariantBuilder(PEBlocks.ENCHANTERS_WORKBENCH.get())
        .partialState().setModels(new ConfiguredModel(models().getExistingFile(modLoc("block/enchanters_workbench"))));
    simpleBlockItem(PEBlocks.ENCHANTERS_WORKBENCH.get(), model);
  }

  void registerPrecisionGrindstone() {
    ModelFile model = models().getExistingFile(modLoc("block/precision_grindstone"));
    getVariantBuilder(PEBlocks.PRECISION_GRINDSTONE.get())
        .partialState().with(PrecisionGrindstoneBlock.FACING, Direction.NORTH).setModels(new ConfiguredModel(model, 0, 0, false))
        .partialState().with(PrecisionGrindstoneBlock.FACING, Direction.EAST).setModels(new ConfiguredModel(model, 0, 90, false))
        .partialState().with(PrecisionGrindstoneBlock.FACING, Direction.SOUTH).setModels(new ConfiguredModel(model, 0, 180, false))
        .partialState().with(PrecisionGrindstoneBlock.FACING, Direction.WEST).setModels(new ConfiguredModel(model, 0, 270, false));
    simpleBlockItem(PEBlocks.PRECISION_GRINDSTONE.get(), model);
  }

  @Override
  protected void registerStatesAndModels() {
    registerEnchantersWorkbench();
    registerPrecisionGrindstone();
  }

}
