package me.whizvox.precisionenchanter.common.compat.cofh;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.ModList;

public class CoFHCompatProxy {

  public boolean isEnchantmentEnabled(Enchantment enchantment) {
    return true;
  }

  private static CoFHCompatProxy instance = null;

  public static CoFHCompatProxy getInstance() {
    return instance;
  }

  public static void init() {
    if (ModList.get().isLoaded("cofh_core")) {
      instance = new CoFHCompatProxyImpl();
    } else {
      instance = new CoFHCompatProxy();
    }
  }

}
