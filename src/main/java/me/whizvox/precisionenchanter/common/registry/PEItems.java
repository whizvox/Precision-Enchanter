package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.item.EnchantedQuillItem;
import me.whizvox.precisionenchanter.common.item.QuillItem;
import me.whizvox.precisionenchanter.common.lib.PELang;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Supplier;

import static me.whizvox.precisionenchanter.PrecisionEnchanter.modLoc;

public class PEItems {

  public static final TagKey<Item> ENCHANTERS_WORKBENCH_TAG = ItemTags.create(PrecisionEnchanter.modLoc("enchanters_workbench"));

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PrecisionEnchanter.MOD_ID);
  private static final List<RegistryObject<? extends Item>> itemsForTab = new ArrayList<>();

  public static void register(IEventBus bus) {
    ITEMS.register(bus);
    bus.addListener(PEItems::onRegisterCreativeModeTab);
  }

  private static void onRegisterCreativeModeTab(final CreativeModeTabEvent.Register event) {
    event.registerCreativeModeTab(modLoc("main"), builder -> builder
        .title(PELang.CREATIVE_MODE_TAB)
        .icon(() -> new ItemStack(ENCHANTERS_WORKBENCHES.get(DyeColor.RED).get()))
        .displayItems((params, out) -> {
          for (DyeColor dye : DyeColor.values()) {
            out.accept(ENCHANTERS_WORKBENCHES.get(dye).get());
          }
          itemsForTab.forEach(item -> out.accept(item.get()));
        }));
  }

  private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier, boolean addToCreativeTab) {
    var obj = ITEMS.register(name, supplier);
    if (addToCreativeTab) {
      itemsForTab.add(obj);
    }
    return obj;
  }

  private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier) {
    return register(name, supplier, true);
  }

  private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> registryObject, boolean addToCreativeTab) {
    return register(registryObject.getKey().location().getPath(), () -> new BlockItem(registryObject.get(), new Item.Properties()), addToCreativeTab);
  }

  private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> registryObject) {
    return registerBlockItem(registryObject, true);
  }

  public static final RegistryObject<BlockItem>
      PRECISION_GRINDSTONE = registerBlockItem(PEBlocks.PRECISION_GRINDSTONE);

  public static final Map<DyeColor, RegistryObject<BlockItem>> ENCHANTERS_WORKBENCHES;

  static {
    var workbenches = new HashMap<DyeColor, RegistryObject<BlockItem>>();
    PEBlocks.ENCHANTERS_WORKBENCHES.forEach((color, blockSup) ->
        workbenches.put(color, registerBlockItem(blockSup, false))
    );
    ENCHANTERS_WORKBENCHES = Collections.unmodifiableMap(workbenches);
  }

  public static final RegistryObject<QuillItem> QUILL = register("quill", QuillItem::new);
  public static final RegistryObject<EnchantedQuillItem> ENCHANTED_QUILL = register("enchanted_quill", EnchantedQuillItem::new);

}
