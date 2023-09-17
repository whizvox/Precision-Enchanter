package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PECreativeModeTab {

  private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), PrecisionEnchanter.MOD_ID);

  public static void register(IEventBus bus) {
    TABS.register(bus);
  }

  public static final RegistryObject<CreativeModeTab> PE_MAIN_TAB = TABS.register("main", () -> CreativeModeTab.builder()
      .title(Component.translatable("itemGroup.precisionenchanter.main"))
      .icon(() -> new ItemStack(PEItems.ENCHANTERS_WORKBENCHES.get(DyeColor.RED).get()))
      .displayItems((displayParameters, output) -> {
        for (DyeColor color : DyeColor.values()) {
          output.accept(PEItems.ENCHANTERS_WORKBENCHES.get(color).get());
        }
        output.accept(PEItems.PRECISION_GRINDSTONE.get());
        output.accept(PEItems.QUILL.get());
        output.accept(PEItems.ENCHANTED_QUILL.get());
      })
      .build()
  );

}
