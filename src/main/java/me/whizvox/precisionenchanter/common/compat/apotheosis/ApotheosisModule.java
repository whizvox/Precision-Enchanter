package me.whizvox.precisionenchanter.common.compat.apotheosis;

public interface ApotheosisModule {

  boolean isEnabled();

  ApotheosisModule EMPTY = () -> false;

}
