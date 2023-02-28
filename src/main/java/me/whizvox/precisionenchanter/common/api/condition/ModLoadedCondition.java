package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.ModList;

public class ModLoadedCondition implements Condition {

  private final String modId;

  public ModLoadedCondition(String modId) {
    this.modId = modId;
  }

  @Override
  public boolean test() {
    return ModList.get().isLoaded(modId);
  }

  public static final Codec<ModLoadedCondition> CODEC = new Codec<>() {

    @Override
    public void encode(ConditionCodecContext ctx, ModLoadedCondition condition, JsonObject out) {
      out.addProperty("mod_id", condition.modId);
    }

    @Override
    public ModLoadedCondition decode(ConditionCodecContext ctx, JsonObject in) {
      String modId = in.get("mod_id").getAsString();
      return new ModLoadedCondition(modId);
    }

  };

}
