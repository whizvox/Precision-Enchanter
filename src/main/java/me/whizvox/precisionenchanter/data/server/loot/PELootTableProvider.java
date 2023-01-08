package me.whizvox.precisionenchanter.data.server.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class PELootTableProvider {

  public static LootTableProvider create(PackOutput output) {
    // not sure what "required tables" refers to
    return new LootTableProvider(output, Set.of(), List.of(
        new LootTableProvider.SubProviderEntry(PEBlockLoot::new, LootContextParamSets.BLOCK)
    ));
  }

}
