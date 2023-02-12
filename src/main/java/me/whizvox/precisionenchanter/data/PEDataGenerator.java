package me.whizvox.precisionenchanter.data;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.data.client.PEBlockStateProvider;
import me.whizvox.precisionenchanter.data.client.PEItemModelProvider;
import me.whizvox.precisionenchanter.data.client.PELanguageProvider;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import me.whizvox.precisionenchanter.data.server.PERecipeProvider;
import me.whizvox.precisionenchanter.data.server.loot.PELootTableProvider;
import me.whizvox.precisionenchanter.data.server.tag.PEBlockTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class PEDataGenerator {

  public static void register(IEventBus bus) {
    bus.addListener(PEDataGenerator::onGatherData);
  }

  private static void onGatherData(final GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();

    gen.addProvider(new PELanguageProvider(gen, "en_us"));
    gen.addProvider(new PEBlockStateProvider(gen, fileHelper));
    gen.addProvider(new PEItemModelProvider(gen, fileHelper));
    gen.addProvider(new PERecipeProvider(gen));
    gen.addProvider(new PEBlockTagProvider(gen, fileHelper));
    gen.addProvider(new PELootTableProvider(gen));
    gen.addProvider(new EnchantmentRecipeProvider(gen, PrecisionEnchanter.MOD_ID));
  }

}
