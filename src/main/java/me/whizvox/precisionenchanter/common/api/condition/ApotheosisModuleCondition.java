package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import shadows.apotheosis.Apotheosis;

import java.util.Map;
import java.util.function.Supplier;

public record ApotheosisModuleCondition(String module) implements Condition {

  public static final String
      MODULE_SPAWNER = "spawner",
      MODULE_GARDEN = "garden",
      MODULE_ADVENTURE = "adventure",
      MODULE_ENCHANTMENT = "enchantment",
      MODULE_POTION = "potion",
      MODULE_VILLAGE = "village";

  public static final ApotheosisModuleCondition
      SPAWNER = new ApotheosisModuleCondition(MODULE_SPAWNER),
      GARDEN = new ApotheosisModuleCondition(MODULE_GARDEN),
      ADVENTURE = new ApotheosisModuleCondition(MODULE_ADVENTURE),
      ENCHANTMENT = new ApotheosisModuleCondition(MODULE_ENCHANTMENT),
      POTION = new ApotheosisModuleCondition(MODULE_POTION),
      VILLAGE = new ApotheosisModuleCondition(MODULE_VILLAGE);

  private static final Map<String, Supplier<Boolean>> MODULES = Map.of(
      MODULE_SPAWNER, () -> Apotheosis.enableSpawner,
      MODULE_GARDEN, () -> Apotheosis.enableGarden,
      MODULE_ADVENTURE, () -> Apotheosis.enableAdventure,
      MODULE_ENCHANTMENT, () -> Apotheosis.enableEnch,
      MODULE_POTION, () -> Apotheosis.enablePotion,
      MODULE_VILLAGE, () -> Apotheosis.enableVillage
  );

  @Override
  public boolean test(LoadStage stage) {
    var supplier = MODULES.get(module);
    if (supplier == null) {
      throw new IllegalArgumentException("Unknown Apotheosis module: " + module);
    }
    return supplier.get();
  }

  public static final Codec<ApotheosisModuleCondition> CODEC = new Codec<ApotheosisModuleCondition>() {

    @Override
    public void encode(ConditionCodecContext ctx, ApotheosisModuleCondition condition, JsonObject out) {
      out.addProperty("module", condition.module);
    }

    @Override
    public ApotheosisModuleCondition decode(ConditionCodecContext ctx, JsonObject in) {
      String module = in.get("module").getAsString();
      if (!MODULES.containsKey(module)) {
        throw new IllegalArgumentException("Unknown Apotheosis module: " + module);
      }
      return new ApotheosisModuleCondition(module);
    }

  };

}
