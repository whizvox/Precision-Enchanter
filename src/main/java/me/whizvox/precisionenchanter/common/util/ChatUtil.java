package me.whizvox.precisionenchanter.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ChatUtil {

  public static MutableComponent mut(Component component) {
    if (component instanceof MutableComponent mut) {
      return mut;
    }
    return component.copy();
  }

  public static MutableComponent reset(Component component) {
    return mut(component).withStyle(ChatFormatting.RESET);
  }

}
