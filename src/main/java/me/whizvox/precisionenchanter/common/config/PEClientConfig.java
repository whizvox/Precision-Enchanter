package me.whizvox.precisionenchanter.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PEClientConfig {

  public final ForgeConfigSpec.ConfigValue<Boolean> displayLevelsAsNumbers;

  PEClientConfig(ForgeConfigSpec.Builder builder) {
    displayLevelsAsNumbers = builder
        .comment("Whether to display enchantment levels in Enchanter's Workbench and Precision Grindstone as roman numerals (false) or plain numbers (true)")
        .define("displayLevelsAsNumbers", false);
  }

}
