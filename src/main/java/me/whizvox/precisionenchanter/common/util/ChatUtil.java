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

  public static MutableComponent mut(Object obj) {
    return obj instanceof Component comp ? mut(comp) : Component.literal(String.valueOf(obj));
  }

  public static final ChatFormatting
      INFO = ChatFormatting.AQUA,
      WARN = ChatFormatting.YELLOW,
      ERROR = ChatFormatting.RED,
      PRIMARY = ChatFormatting.GREEN,
      SECONDARY = ChatFormatting.GRAY;

}
