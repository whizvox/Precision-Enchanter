package me.whizvox.precisionenchanter.common.api.condition;

public enum LoadStage {

  /**
   * When all data is loaded for the first time. During this stage, it is not guaranteed that some things are not yet
   * loaded, such as tags and configuration files.
   */
  LOAD,
  /**
   * After all data has been loaded (hopefully). At this point, everything should be loaded.
   */
  POST_LOAD

}
