package me.whizvox.precisionenchanter.common.lib;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import net.minecraftforge.fml.DistExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class PELog {

  public static final Logger LOGGER = LoggerFactory.getLogger(PrecisionEnchanter.class.getSimpleName());

  public static final Marker
      M_DATAGEN = MarkerFactory.getMarker("DataGen"),
      M_REGISTRY = MarkerFactory.getMarker("Registry"),
      M_CLIENT_INIT = MarkerFactory.getMarker("ClientInit"),
      M_CLIENT = MarkerFactory.getMarker("Client"),
      M_COMMON = MarkerFactory.getMarker("Common"),
      M_SERVER = MarkerFactory.getMarker("Server");

  public static Marker side() {
    return DistExecutor.safeRunForDist(() -> () -> M_CLIENT, () -> () -> M_SERVER);
  }

}
