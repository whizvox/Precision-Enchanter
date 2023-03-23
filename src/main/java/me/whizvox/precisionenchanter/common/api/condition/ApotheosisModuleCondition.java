package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import me.whizvox.precisionenchanter.common.compat.apotheosis.ApotheosisCompatProxy;

public record ApotheosisModuleCondition(String module) implements NonDeferredCondition {

  public static final String
      MODULE_SPAWNER = "spawner",
      MODULE_GARDEN = "garden",
      MODULE_ADVENTURE = "adventure",
      MODULE_ENCHANTING = "enchanting",
      MODULE_POTION = "potion",
      MODULE_VILLAGE = "village";

  public static final ApotheosisModuleCondition
      SPAWNER = new ApotheosisModuleCondition(MODULE_SPAWNER),
      GARDEN = new ApotheosisModuleCondition(MODULE_GARDEN),
      ADVENTURE = new ApotheosisModuleCondition(MODULE_ADVENTURE),
      ENCHANTING = new ApotheosisModuleCondition(MODULE_ENCHANTING),
      POTION = new ApotheosisModuleCondition(MODULE_POTION),
      VILLAGE = new ApotheosisModuleCondition(MODULE_VILLAGE);

  @Override
  public boolean test() {
    return ApotheosisCompatProxy.getInstance().isModuleEnabled(module);
  }

  public static final Codec<ApotheosisModuleCondition> CODEC = new Codec<>() {

    @Override
    public void encode(ConditionCodecContext ctx, ApotheosisModuleCondition condition, JsonObject out) {
      out.addProperty("module", condition.module);
    }

    @Override
    public ApotheosisModuleCondition decode(ConditionCodecContext ctx, JsonObject in) {
      String module = in.get("module").getAsString();
      return new ApotheosisModuleCondition(module);
    }

  };

}
