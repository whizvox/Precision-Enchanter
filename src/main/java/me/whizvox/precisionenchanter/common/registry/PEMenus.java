package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import me.whizvox.precisionenchanter.common.menu.PrecisionGrindstoneMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PEMenus {

  private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, PrecisionEnchanter.MOD_ID);

  public static void register(IEventBus bus) {
    MENUS.register(bus);
  }

  private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
    return MENUS.register(name, () -> IForgeMenuType.create(factory));
  }

  public static final RegistryObject<MenuType<EnchantersWorkbenchMenu>> ENCHANTERS_WORKBENCH = register("enchanters_workbench", EnchantersWorkbenchMenu::new);
  public static final RegistryObject<MenuType<PrecisionGrindstoneMenu>> PRECISION_GRINDSTONE = register("precision_grindstone", PrecisionGrindstoneMenu::new);

}
