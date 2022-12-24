package me.whizvox.precisionenchanter.common;

import me.whizvox.precisionenchanter.client.screen.EnchantersWorkbenchScreen;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import me.whizvox.precisionenchanter.common.registry.PEItems;
import me.whizvox.precisionenchanter.common.registry.PEMenus;
import me.whizvox.precisionenchanter.common.util.PEEnchantmentHelper;
import me.whizvox.precisionenchanter.data.PEDataGenerator;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PrecisionEnchanter.MOD_ID)
public class PrecisionEnchanter {

  public static final String MOD_ID = "precisionenchanter";

  public static ResourceLocation modLoc(String name) {
    return new ResourceLocation(MOD_ID, name);
  }

  public PrecisionEnchanter() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    IEventBus forgeBus = MinecraftForge.EVENT_BUS;

    PEBlocks.register(modBus);
    PEItems.register(modBus);
    PEMenus.register(modBus);
    PEDataGenerator.register(modBus);
    PENetwork.register();
    modBus.addListener(this::onClientSetup);

    PEEnchantmentHelper.register(forgeBus);
    forgeBus.addListener(this::addPrecisionEnchantmentRecipeListener);
  }

  private void addPrecisionEnchantmentRecipeListener(final AddReloadListenerEvent event) {
    event.addListener(EnchantmentRecipeManager.INSTANCE);
  }

  private void onClientSetup(final FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      MenuScreens.register(PEMenus.PRECISION_ENCHANTMENT_TABLE.get(), EnchantersWorkbenchScreen::new);
    });
  }

}
