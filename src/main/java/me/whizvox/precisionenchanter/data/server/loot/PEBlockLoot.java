package me.whizvox.precisionenchanter.data.server.loot;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class PEBlockLoot extends BlockLoot {

  private final List<Block> knownBlocks;

  protected PEBlockLoot() {
    knownBlocks = new ObjectArrayList<>();
    ForgeRegistries.BLOCKS.getKeys().stream()
        .filter(loc -> loc.getNamespace().equals(PrecisionEnchanter.MOD_ID))
        .map(ForgeRegistries.BLOCKS::getValue)
        .forEach(knownBlocks::add);
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return knownBlocks;
  }

  @Override
  protected void addTables() {
    dropSelf(PEBlocks.ENCHANTERS_WORKBENCH.get());
    dropSelf(PEBlocks.PRECISION_GRINDSTONE.get());
  }

}
