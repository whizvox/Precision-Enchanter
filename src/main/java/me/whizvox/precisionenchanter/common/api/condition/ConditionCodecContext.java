package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionCodecContext {

  private final Map<ResourceLocation, Condition.Codec<?>> codecs;
  private final Map<Class<?>, ResourceLocation> classIdMap;
  private final Map<Class<?>, Condition.Codec<?>> classCodecMap;

  public ConditionCodecContext() {
    codecs = new HashMap<>();
    classIdMap = new HashMap<>();
    classCodecMap = new HashMap<>();
  }

  protected <C extends Condition> void register(ResourceLocation id, Condition.Codec<C> codec, Class<C> cls) {
    codecs.put(id, codec);
    classIdMap.put(cls, id);
    classCodecMap.put(cls, codec);
  }

  @Nullable
  public <C extends Condition> Condition.Codec<C> getCodec(ResourceLocation id) {
    try {
      //noinspection unchecked
      return (Condition.Codec<C>) codecs.get(id);
    } catch (ClassCastException e) {
      return null;
    }
  }

  public <C extends Condition> C parse(JsonObject obj) {
    String idStr = obj.get("condition").getAsString();
    String namespace;
    String path;
    int indexOfSeparator = idStr.indexOf(':');
    if (indexOfSeparator >= 0) {
      namespace = idStr.substring(0, indexOfSeparator);
      path = idStr.substring(indexOfSeparator + 1);
    } else {
      namespace = PrecisionEnchanter.MOD_ID;
      path = idStr;
    }
    ResourceLocation id;
    try {
      id = new ResourceLocation(namespace, path);
    } catch (ResourceLocationException e) {
      throw new JsonParseException(e);
    }
    Condition.Codec<C> codec = getCodec(id);
    if (codec == null) {
      throw new JsonParseException("Unknown condition: " + id);
    }
    return codec.decode(this, obj);
  }

  public <C extends Condition> JsonObject encode(C condition) {
    Class<?> cls = condition.getClass();
    Condition.Codec<C> codec;
    try {
      //noinspection unchecked
      codec = (Condition.Codec<C>) classCodecMap.get(cls);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Unknown condition class: " + condition.getClass());
    }
    JsonObject conditionObj = new JsonObject();
    ResourceLocation id = classIdMap.get(cls);
    String idStr;
    if (id.getNamespace().equals(PrecisionEnchanter.MOD_ID)) {
      idStr = id.getPath();
    } else {
      idStr = id.toString();
    }
    conditionObj.addProperty("condition", idStr);
    codec.encode(this, condition, conditionObj);
    return conditionObj;
  }

  private static final Condition.Codec<AndCondition> TOP_LEVEL_CODEC = CollectionCondition.createCodec(AndCondition::new, "conditions");

  public Condition parseAllConditions(JsonObject in) {
    if (in.has("conditions")) {
      return TOP_LEVEL_CODEC.decode(this, in);
    }
    return Condition.TAUTOLOGY;
  }

  public <C extends Condition> void writeAllConditions(C condition, JsonObject out) {
    if (condition instanceof AndCondition andCondition) {
      TOP_LEVEL_CODEC.encode(this, andCondition, out);
    } else if (condition != null && condition != Condition.TAUTOLOGY) {
      AndCondition finalCondition = new AndCondition(List.of(condition));
      TOP_LEVEL_CODEC.encode(this, finalCondition, out);
    }
  }

}
