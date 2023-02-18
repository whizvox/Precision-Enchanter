package me.whizvox.precisionenchanter.data;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.data.client.PEBlockStateProvider;
import me.whizvox.precisionenchanter.data.client.PEItemModelProvider;
import me.whizvox.precisionenchanter.data.client.PELanguageProvider;
import me.whizvox.precisionenchanter.data.server.EnchantmentRecipeProvider;
import me.whizvox.precisionenchanter.data.server.PERecipeProvider;
import me.whizvox.precisionenchanter.data.server.compat.TwilightForestEnchantmentRecipeProvider;
import me.whizvox.precisionenchanter.data.server.loot.PELootTableProvider;
import me.whizvox.precisionenchanter.data.server.tag.PEBlockTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This is the only class/method to resort to using annotations to register events. This is because the entire data
 * package is not included in the final distributed JAR file. This is due to 2 reasons:
 * <ol>
 *   <li>Data generation code is not used outside of development. So the final JAR file is smaller than if the data
 *   generation code was included.</li>
 *   <li>This package uses code from other mods to generate recipes for their enchantments. These mods will most likely
 *   not exist in the environments of the end users. So if this package is excluded, there won't be any
 *   {@link ClassNotFoundException}s.</li>
 * </ol>
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = PrecisionEnchanter.MOD_ID)
public class PEDataGenerator {

  @SubscribeEvent
  public static void onGatherData(final GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    PackOutput output = gen.getPackOutput();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();
    var lookupProvider = event.getLookupProvider();
    boolean includeClient = event.includeClient();
    boolean includeServer = event.includeServer();

    gen.addProvider(includeClient, new PELanguageProvider(output, "en_us"));
    gen.addProvider(includeClient, new PEBlockStateProvider(output, fileHelper));
    gen.addProvider(includeClient, new PEItemModelProvider(output, fileHelper));
    gen.addProvider(includeServer, new PERecipeProvider(output));
    gen.addProvider(includeServer, new PEBlockTagProvider(output, lookupProvider, fileHelper));
    gen.addProvider(includeServer, PELootTableProvider.create(output));
    gen.addProvider(includeServer, new EnchantmentRecipeProvider(gen, PrecisionEnchanter.MOD_ID));
    gen.addProvider(includeServer, new TwilightForestEnchantmentRecipeProvider(gen, PrecisionEnchanter.MOD_ID));
  }

}
