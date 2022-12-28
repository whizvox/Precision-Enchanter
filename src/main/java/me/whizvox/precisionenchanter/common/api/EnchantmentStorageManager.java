package me.whizvox.precisionenchanter.common.api;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.api.internal.BookEnchantmentStorage;
import me.whizvox.precisionenchanter.common.api.internal.EnchantableItemEnchantmentStorage;
import me.whizvox.precisionenchanter.common.api.internal.EnchantedBookEnchantmentStorage;
import me.whizvox.precisionenchanter.common.lib.PELog;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class EnchantmentStorageManager {

  private final List<RegisterEnchantmentStorageMessage> codecsList;
  private final PriorityQueue<RegisterEnchantmentStorageMessage> codecsQueue;
  private boolean modified;

  private EnchantmentStorageManager() {
    codecsList = new ArrayList<>();
    // descending order
    codecsQueue = new PriorityQueue<>((o1, o2) -> o2.priority() - o1.priority());

    register(new RegisterEnchantmentStorageMessage(new EnchantableItemEnchantmentStorage(), PrecisionEnchanter.MOD_ID, 0));
    register(new RegisterEnchantmentStorageMessage(new EnchantedBookEnchantmentStorage(), PrecisionEnchanter.MOD_ID, 1));
    register(new RegisterEnchantmentStorageMessage(new BookEnchantmentStorage(), PrecisionEnchanter.MOD_ID, 1));
    rebuildCaches();
  }

  private void rebuildCaches() {
    if (modified) {
      PELog.LOGGER.info("Rebuilding enchantment storage manager cache with {} entries", codecsQueue.size());
      codecsList.clear();
      PriorityQueue<RegisterEnchantmentStorageMessage> copy = new PriorityQueue<>(codecsQueue);
      while (!copy.isEmpty()) {
        codecsList.add(copy.remove());
      }
      modified = false;
    }
  }

  private void register(RegisterEnchantmentStorageMessage msg) {
    codecsQueue.add(msg);
    modified = true;
  }

  @Nullable
  public IEnchantmentStorage findMatch(ItemStack stack) {
    rebuildCaches();
    for (RegisterEnchantmentStorageMessage msg : codecsList) {
      if (msg.codec().accepts(stack)) {
        return msg.codec();
      }
    }
    return null;
  }

  public static final EnchantmentStorageManager INSTANCE = new EnchantmentStorageManager();

  public static void register(IEventBus bus) {
    bus.addListener(EnchantmentStorageManager::onProcessIMC);
  }

  private static void onProcessIMC(final InterModProcessEvent event) {
    InterModComms.getMessages(PrecisionEnchanter.MOD_ID, s -> s.equals("RegisterEnchantmentStorage")).forEach(imcMsg -> {
      Object rawMsg = imcMsg.messageSupplier().get();
      if (rawMsg == null || !RegisterEnchantmentStorageMessage.class.isAssignableFrom(rawMsg.getClass())) {
        PELog.LOGGER.warn(PELog.side(), "Attempted to register a bad codec");
      } else {
        RegisterEnchantmentStorageMessage msg = (RegisterEnchantmentStorageMessage) rawMsg;
        INSTANCE.register(msg);
      }
    });
  }

}
