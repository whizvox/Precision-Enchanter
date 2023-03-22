package me.whizvox.precisionenchanter.common.lib;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.api.condition.*;
import net.minecraft.resources.ResourceLocation;

public class PEConditionCodecContext extends ConditionCodecContext {

  public PEConditionCodecContext() {
    register("not", NotCondition.CODEC, NotCondition.class);
    register("and", AndCondition.CODEC, AndCondition.class);
    register("or", OrCondition.CODEC, OrCondition.class);
    register("xor", XorCondition.CODEC, XorCondition.class);
    register("mod_loaded", ModLoadedCondition.CODEC, ModLoadedCondition.class);
    register("apotheosis_module", ApotheosisModuleCondition.CODEC, ApotheosisModuleCondition.class);
    register("tag_exists", TagExistsCondition.CODEC, TagExistsCondition.class);
    register("cofh_enabled", CoFHEnabledCondition.CODEC, CoFHEnabledCondition.class);
  }

  private <C extends Condition> void register(String id, Condition.Codec<C> codec, Class<C> cls) {
    register(new ResourceLocation(PrecisionEnchanter.MOD_ID, id), codec, cls);
  }

  public static final ConditionCodecContext INSTANCE = new PEConditionCodecContext();

}
