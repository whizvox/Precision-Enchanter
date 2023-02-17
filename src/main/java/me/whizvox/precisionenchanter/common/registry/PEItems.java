package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.item.EnchantedQuillItem;
import me.whizvox.precisionenchanter.common.item.QuillItem;
import me.whizvox.precisionenchanter.common.lib.PELang;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static me.whizvox.precisionenchanter.PrecisionEnchanter.modLoc;

public class PEItems {

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PrecisionEnchanter.MOD_ID);
  private static final List<RegistryObject<? extends Item>> itemsForTab = new ArrayList<>();

  public static void register(IEventBus bus) {
    ITEMS.register(bus);
    bus.addListener(PEItems::onRegisterCreativeModeTab);
  }

  private static void onRegisterCreativeModeTab(final CreativeModeTabEvent.Register event) {
    event.registerCreativeModeTab(modLoc("main"), builder -> builder
        .title(PELang.CREATIVE_MODE_TAB)
        .icon(() -> new ItemStack(PEItems.ENCHANTERS_WORKBENCH.get()))
        .displayItems((featureFlags, out, isOp) -> itemsForTab.forEach(item -> out.accept(item.get()))));
  }

  private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier) {
    var obj = ITEMS.register(name, supplier);
    itemsForTab.add(obj);
    return obj;
  }

  private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> registryObject) {
    return register(registryObject.getKey().location().getPath(), () -> new BlockItem(registryObject.get(), new Item.Properties()));
  }

  public static final RegistryObject<QuillItem> QUILL = register("quill", QuillItem::new);
  public static final RegistryObject<EnchantedQuillItem> ENCHANTED_QUILL = register("enchanted_quill", EnchantedQuillItem::new);

  public static final RegistryObject<BlockItem>
      ENCHANTERS_WORKBENCH = registerBlockItem(PEBlocks.ENCHANTERS_WORKBENCH),
      PRECISION_GRINDSTONE = registerBlockItem(PEBlocks.PRECISION_GRINDSTONE);

}
