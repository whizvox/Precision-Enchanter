package me.whizvox.precisionenchanter.common.api;

import net.minecraft.resources.ResourceLocation;

public class NoSuchEnchantmentException extends RuntimeException {

  public final ResourceLocation id;

  public NoSuchEnchantmentException(ResourceLocation id) {
    this.id = id;
  }

}
