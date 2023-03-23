package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.registries.ForgeRegistries;

public record TagExistsCondition(ResourceLocation tag) implements DeferredCondition {

  @Override
  public boolean test() {
    //noinspection DataFlowIssue
    return !ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(tag)).isEmpty();
  }

  public static final Codec<TagExistsCondition> CODEC = new Codec<>() {

    @Override
    public void encode(ConditionCodecContext ctx, TagExistsCondition condition, JsonObject out) {
      out.addProperty("tag", condition.tag.toString());
    }

    @Override
    public TagExistsCondition decode(ConditionCodecContext ctx, JsonObject in) {
      ResourceLocation tag = new ResourceLocation(in.get("tag").getAsString());
      return new TagExistsCondition(tag);
    }

  };

}
