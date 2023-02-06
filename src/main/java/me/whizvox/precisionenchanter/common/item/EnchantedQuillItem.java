package me.whizvox.precisionenchanter.common.item;

import me.whizvox.precisionenchanter.common.PECreativeTab;
import net.minecraft.world.item.Item;

public class EnchantedQuillItem extends QuillItem {

  public EnchantedQuillItem() {
    super(new Item.Properties().stacksTo(1).tab(PECreativeTab.INSTANCE));
  }

}
