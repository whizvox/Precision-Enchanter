package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.block.EnchantersWorkbenchBlock;
import me.whizvox.precisionenchanter.common.block.PrecisionGrindstoneBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PEBlocks {

  public static final TagKey<Block> ENCHANTERS_WORKBENCH_TAG = BlockTags.create(PrecisionEnchanter.modLoc("enchanters_workbench"));

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PrecisionEnchanter.MOD_ID);

  public static void register(IEventBus bus) {
    BLOCKS.register(bus);
  }

  public static final Map<DyeColor, RegistryObject<EnchantersWorkbenchBlock>> ENCHANTERS_WORKBENCHES;

  static {
    var workbenches = new HashMap<DyeColor, RegistryObject<EnchantersWorkbenchBlock>>();
    for (DyeColor dye : DyeColor.values()) {
      workbenches.put(dye, BLOCKS.register(dye.getName() + "_enchanters_workbench", () -> new EnchantersWorkbenchBlock(dye)));
    }
    ENCHANTERS_WORKBENCHES = Collections.unmodifiableMap(workbenches);
  }

  public static final RegistryObject<PrecisionGrindstoneBlock> PRECISION_GRINDSTONE = BLOCKS.register("precision_grindstone", PrecisionGrindstoneBlock::new);

}
