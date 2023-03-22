package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.block.EnchantersWorkbenchBlock;
import me.whizvox.precisionenchanter.common.block.PrecisionGrindstoneBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PEBlocks {

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PrecisionEnchanter.MOD_ID);

  public static void register(IEventBus bus) {
    BLOCKS.register(bus);
  }

  public static final RegistryObject<EnchantersWorkbenchBlock> ENCHANTERS_WORKBENCH = BLOCKS.register("enchanters_workbench", EnchantersWorkbenchBlock::new);
  public static final RegistryObject<PrecisionGrindstoneBlock> PRECISION_GRINDSTONE = BLOCKS.register("precision_grindstone", PrecisionGrindstoneBlock::new);

}
