package me.whizvox.precisionenchanter.data;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.data.client.PEBlockStateProvider;
import me.whizvox.precisionenchanter.data.client.PEItemModelProvider;
import me.whizvox.precisionenchanter.data.client.PELanguageProvider;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import me.whizvox.precisionenchanter.data.server.PERecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class PEDataGenerator {

  public static void register(IEventBus bus) {
    bus.addListener(PEDataGenerator::onGatherData);
  }

  private static void onGatherData(final GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    PackOutput output = gen.getPackOutput();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();
    boolean includeClient = event.includeClient();
    boolean includeServer = event.includeServer();

    gen.addProvider(includeClient, new PELanguageProvider(output, "en_us"));
    gen.addProvider(includeClient, new PEBlockStateProvider(output, fileHelper));
    gen.addProvider(includeClient, new PEItemModelProvider(output, fileHelper));
    gen.addProvider(includeServer, new PERecipeProvider(output));
    gen.addProvider(includeServer, new EnchantmentRecipeProvider(gen, PrecisionEnchanter.MOD_ID));
  }

}
