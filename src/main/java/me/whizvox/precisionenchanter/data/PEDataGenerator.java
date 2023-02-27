package me.whizvox.precisionenchanter.data;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.data.client.PEBlockStateProvider;
import me.whizvox.precisionenchanter.data.client.PEItemModelProvider;
import me.whizvox.precisionenchanter.data.client.PELanguageProvider;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import me.whizvox.precisionenchanter.data.server.PERecipeProvider;
import me.whizvox.precisionenchanter.data.server.compat.CoFHCoreEnchantmentRecipeProvider;
import me.whizvox.precisionenchanter.data.server.compat.EnsorcellationEnchantmentRecipeProvider;
import me.whizvox.precisionenchanter.data.server.loot.PELootTableProvider;
import me.whizvox.precisionenchanter.data.server.tag.PEBlockTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class PEDataGenerator {

  public static void register(IEventBus bus) {
    bus.addListener(PEDataGenerator::onGatherData);
  }

  private static void onGatherData(final GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();
    boolean includeClient = event.includeClient();
    boolean includeServer = event.includeServer();

    gen.addProvider(includeClient, new PELanguageProvider(gen, "en_us"));
    gen.addProvider(includeClient, new PEBlockStateProvider(gen, fileHelper));
    gen.addProvider(includeClient, new PEItemModelProvider(gen, fileHelper));
    gen.addProvider(includeServer, new PERecipeProvider(gen));
    gen.addProvider(includeServer, new PEBlockTagProvider(gen, fileHelper));
    gen.addProvider(includeServer, new PELootTableProvider(gen));
    gen.addProvider(includeServer, new EnchantmentRecipeProvider(gen, PrecisionEnchanter.MOD_ID));
    gen.addProvider(includeServer, new CoFHCoreEnchantmentRecipeProvider(gen, PrecisionEnchanter.MOD_ID));
    gen.addProvider(includeServer, new EnsorcellationEnchantmentRecipeProvider(gen, PrecisionEnchanter.MOD_ID));
  }

}
