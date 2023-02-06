package me.whizvox.precisionenchanter.data.server.loot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PELootTableProvider extends LootTableProvider {

  public PELootTableProvider(DataGenerator gen) {
    super(gen);
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
    return List.of(
        Pair.of(PEBlockLoot::new, LootContextParamSets.BLOCK)
    );
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    // not necessary
  }

}
