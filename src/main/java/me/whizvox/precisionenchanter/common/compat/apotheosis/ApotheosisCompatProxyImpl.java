package me.whizvox.precisionenchanter.common.compat.apotheosis;

// UNCOMMENT IF APOTHEOSIS GETS PORTED TO 1.19.3

public class ApotheosisCompatProxyImpl extends ApotheosisCompatProxy {

  /*public enum Module implements ApotheosisModule {
    ADVENTURE(() -> Apotheosis.enableAdventure),
    ENCHANTING(() -> Apotheosis.enableEnch),
    GARDEN(() -> Apotheosis.enableGarden),
    POTION(() -> Apotheosis.enablePotion),
    SPAWNER(() -> Apotheosis.enableSpawner),
    VILLAGE(() -> Apotheosis.enableVillage);

    private final Supplier<Boolean> enabledSupplier;

    Module(Supplier<Boolean> enabledSupplier) {
      this.enabledSupplier = enabledSupplier;
    }

    @Override
    public boolean isEnabled() {
      return enabledSupplier.get();
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

  @Override
  public boolean isModuleEnabled(String module) {
    return MODULES.getOrDefault(module, ApotheosisModule.EMPTY).isEnabled();
  }*/

}
