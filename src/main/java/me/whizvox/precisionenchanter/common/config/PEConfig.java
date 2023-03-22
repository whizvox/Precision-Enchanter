package me.whizvox.precisionenchanter.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class PEConfig {

  public static final PEClientConfig CLIENT;

  private static final ForgeConfigSpec CLIENT_SPEC;

  static {
    var clientPair = new ForgeConfigSpec.Builder().configure(PEClientConfig::new);
    CLIENT = clientPair.getLeft();
    CLIENT_SPEC = clientPair.getRight();
  }

  public static void register(ModLoadingContext ctx) {
    ctx.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
  }

}
