package me.whizvox.precisionenchanter.common.registry;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.item.EnchantedQuillItem;
import me.whizvox.precisionenchanter.common.item.QuillItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PEItems {

  public static final TagKey<Item> ENCHANTERS_WORKBENCH_TAG = ItemTags.create(PrecisionEnchanter.modLoc("enchanters_workbench"));

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PrecisionEnchanter.MOD_ID);

  public static void register(IEventBus bus) {
    ITEMS.register(bus);
  }

  private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> registryObject) {
    return ITEMS.register(registryObject.getKey().location().getPath(), () -> new BlockItem(registryObject.get(), new Item.Properties()));
  }

  public static final RegistryObject<QuillItem> QUILL = ITEMS.register("quill", QuillItem::new);
  public static final RegistryObject<EnchantedQuillItem> ENCHANTED_QUILL = ITEMS.register("enchanted_quill", EnchantedQuillItem::new);

  public static final Map<DyeColor, RegistryObject<BlockItem>> ENCHANTERS_WORKBENCHES;
  public static final RegistryObject<BlockItem>
      PRECISION_GRINDSTONE = registerBlockItem(PEBlocks.PRECISION_GRINDSTONE);

  static {
    var map = new HashMap<DyeColor, RegistryObject<BlockItem>>();
    PEBlocks.ENCHANTERS_WORKBENCHES.forEach((color, workbenchRegObj) ->
        map.put(color, registerBlockItem(workbenchRegObj))
    );
    ENCHANTERS_WORKBENCHES = Collections.unmodifiableMap(map);
  }

}
