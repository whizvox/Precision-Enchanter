package me.whizvox.precisionenchanter.common.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

/**
 * The message that should be sent if a mod has its own special way of storing enchantments and wishes to be compatible
 * with Precision Enchanter. The exact method is "RegisterEnchantmentStorageCodec" (no quotes). If the result of
 * {@link IEnchantmentStorage#canApply(ItemStack, EnchantmentInstance)} yields true, then any remaining codecs that
 * have yet to be checked are ignored.
 * @param codec The codec that Precision Enchanter will use to read and write enchantments
 * @param originModId The mod ID of the mod that is sending this message
 * @param priority How much priority is given to this codec. Higher number = higher priority. Precision Enchanter uses
 *                 a priority of 0 for normal enchantable items and a priority of 1 for enchanted books and normal
 *                 books. This means that the enchanted book and book codecs will always be checked first before
 *                 checking normal enchantable items.
 */
public record RegisterEnchantmentStorageMessage(IEnchantmentStorage codec, String originModId, int priority) {

}
