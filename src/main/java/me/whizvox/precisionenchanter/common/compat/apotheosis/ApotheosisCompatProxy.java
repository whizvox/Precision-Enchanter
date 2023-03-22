package me.whizvox.precisionenchanter.common.compat.apotheosis;

import net.minecraftforge.fml.ModList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApotheosisCompatProxy {

  public enum Module implements ApotheosisModule {
    ADVENTURE,
    ENCHANTING,
    GARDEN,
    POTION,
    SPAWNER,
    VILLAGE;

    @Override
    public boolean isEnabled() {
      return false;
    }
  }

  private static final Map<String, ApotheosisModule> MODULES;

  static {
    Map<String, ApotheosisModule> modules = new HashMap<>();
    for (Module module : Module.values()) {
      modules.put(module.name().toLowerCase(), module);
    }
    MODULES = Collections.unmodifiableMap(modules);
  }

  public boolean isModuleEnabled(String module) {
    return MODULES.getOrDefault(module, ApotheosisModule.EMPTY).isEnabled();
  }

  private static ApotheosisCompatProxy instance = null;

  public static ApotheosisCompatProxy getInstance() {
    return instance;
  }

  public static void init() {
    if (ModList.get().isLoaded("apotheosis")) {
      instance = new ApotheosisCompatProxyImpl();
    } else {
      instance = new ApotheosisCompatProxy();
    }
  }

}
