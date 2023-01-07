package me.whizvox.precisionenchanter.data.server.loot;

import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

public class PEBlockLoot extends BlockLootSubProvider {

  private static final Iterable<Block> KNOWN_BLOCKS = List.of(
      PEBlocks.ENCHANTERS_WORKBENCH.get(), PEBlocks.PRECISION_GRINDSTONE.get()
  );

  protected PEBlockLoot() {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags());
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return KNOWN_BLOCKS;
  }

  @Override
  protected void generate() {
    dropSelf(PEBlocks.ENCHANTERS_WORKBENCH.get());
    dropSelf(PEBlocks.PRECISION_GRINDSTONE.get());
  }

}
