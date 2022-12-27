package me.whizvox.precisionenchanter.common.util;

import me.whizvox.precisionenchanter.common.network.PENetwork;
import me.whizvox.precisionenchanter.common.network.message.SyncEnchantmentsMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PEEnchantmentHelper {

  private final List<Enchantment> enchantments;
  private final Map<ResourceLocation, Integer> byNumId;

  public PEEnchantmentHelper() {
    enchantments = new ArrayList<>();
    byNumId = new HashMap<>();
  }

  private void clear() {
    enchantments.clear();
    byNumId.clear();
  }

  private void add(Enchantment enchantment) {
    int numId = enchantments.size();
    enchantments.add(enchantment);
    byNumId.put(ForgeRegistries.ENCHANTMENTS.getKey(enchantment), numId);
  }

  private void init() {
    clear();
    ForgeRegistries.ENCHANTMENTS.forEach(this::add);
  }

  public void sync(List<Enchantment> enchantments) {
    clear();
    enchantments.forEach(this::add);
  }

  public SyncEnchantmentsMessage createSyncMessage() {
    return new SyncEnchantmentsMessage(Collections.unmodifiableList(enchantments));
  }

  @Nullable
  public Enchantment get(int id) {
    if (id < 0 || id >= enchantments.size()) {
      return null;
    }
    return enchantments.get(id);
  }

  public Integer getId(Enchantment enchantment) {
    return byNumId.get(ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
  }

  public static final PEEnchantmentHelper INSTANCE = new PEEnchantmentHelper();

  public static void register(IEventBus bus) {
    bus.addListener(PEEnchantmentHelper::onServerStarting);
    bus.addListener(PEEnchantmentHelper::onServerStopping);
    bus.addListener(PEEnchantmentHelper::onPlayerLogin);
  }

  private static void onServerStarting(final ServerStartingEvent event) {
    INSTANCE.init();
  }

  private static void onServerStopping(final ServerStoppingEvent event) {
    INSTANCE.clear();
  }

  private static void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
    PENetwork.sendToClient(INSTANCE.createSyncMessage(), (ServerPlayer) event.getEntity());
  }

  public static int estimateEnchantmentWorth(EnchantmentInstance instance) {
    return instance.level * switch (instance.enchantment.getRarity()) {
      default -> 1;
      case UNCOMMON -> 2;
      case RARE -> 4;
      case VERY_RARE -> 8;
    };
  }

}
