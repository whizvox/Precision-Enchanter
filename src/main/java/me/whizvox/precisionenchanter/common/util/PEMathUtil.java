package me.whizvox.precisionenchanter.common.util;

public class PEMathUtil {

  public static int rollover(int value, int max, int amountToAdd) {
    value = (value + amountToAdd) % max;
    if (value < 0) {
      value = max + value;
    }
    return value;
  }

}
