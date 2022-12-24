package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.menu.EnchantersWorkbenchMenu;
import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.SimpleServerBoundMessage;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PEMenus {

  private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PrecisionEnchanter.MOD_ID);

  public static void register(IEventBus bus) {
    MENUS.register(bus);
  }

  public static final RegistryObject<MenuType<EnchantersWorkbenchMenu>> PRECISION_ENCHANTMENT_TABLE =
      MENUS.register("enchanters_workbench", () -> IForgeMenuType.create(EnchantersWorkbenchMenu::new));

}
