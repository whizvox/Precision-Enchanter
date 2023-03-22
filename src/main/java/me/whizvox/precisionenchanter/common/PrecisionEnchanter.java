package me.whizvox.precisionenchanter.common;

import me.whizvox.precisionenchanter.client.screen.EnchantersWorkbenchScreen;
import me.whizvox.precisionenchanter.client.screen.PrecisionGrindstoneScreen;
import me.whizvox.precisionenchanter.common.api.EnchantmentStorageManager;
import me.whizvox.precisionenchanter.common.compat.apotheosis.ApotheosisCompatProxy;
import me.whizvox.precisionenchanter.common.compat.cofh.CoFHCompatProxy;
import me.whizvox.precisionenchanter.common.config.PEConfig;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import me.whizvox.precisionenchanter.common.registry.PEItems;
import me.whizvox.precisionenchanter.common.registry.PEMenus;
import me.whizvox.precisionenchanter.common.util.PEEnchantmentHelper;
import me.whizvox.precisionenchanter.server.PECommand;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
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
    ModLoadingContext ctx = ModLoadingContext.get();
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    IEventBus forgeBus = MinecraftForge.EVENT_BUS;

    PEConfig.register(ctx);

    PEBlocks.register(modBus);
    PEItems.register(modBus);
    PEMenus.register(modBus);
    PENetwork.register();
    EnchantmentStorageManager.register(modBus);
    modBus.addListener(this::onClientSetup);
    CoFHCompatProxy.init();
    ApotheosisCompatProxy.init();

    PEEnchantmentHelper.register(forgeBus);
    forgeBus.addListener(this::onRegisterCommands);
    forgeBus.addListener(this::onAddReloadListener);
  }

  private void onClientSetup(final FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      MenuScreens.register(PEMenus.ENCHANTERS_WORKBENCH.get(), EnchantersWorkbenchScreen::new);
      MenuScreens.register(PEMenus.PRECISION_GRINDSTONE.get(), PrecisionGrindstoneScreen::new);
    });
  }

  private void onRegisterCommands(final RegisterCommandsEvent event) {
    PECommand.register(event.getDispatcher());
  }

  private void onAddReloadListener(final AddReloadListenerEvent event) {
    event.addListener(EnchantmentRecipeManager.INSTANCE);
  }

}
