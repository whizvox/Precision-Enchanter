package me.whizvox.precisionenchanter.common.item;

import me.whizvox.precisionenchanter.common.util.InventoryUtil;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class QuillItem extends Item {

  public QuillItem(Item.Properties props) {
    super(props);
  }

  public QuillItem() {
    this(new Item.Properties().durability(50));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack quillStack = player.getItemInHand(hand);
    if (!level.isClientSide) {
      InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      ItemStack bookStack = player.getItemInHand(otherHand);
      if (bookStack.is(Items.WRITTEN_BOOK)) {
        boolean canEdit;
        boolean isCreative = player.getAbilities().instabuild;
        boolean hasDurability = !quillStack.isDamageableItem() || quillStack.getDamageValue() < quillStack.getMaxDamage();
        if (isCreative) {
          canEdit = true;
        } else if (hasDurability) {
          UUID authorId = InventoryUtil.getAuthorId(bookStack);
          if (authorId == null) {
            canEdit = player.getGameProfile().getName().equals(bookStack.getTag().getString(WrittenBookItem.TAG_AUTHOR));
          } else {
            canEdit = authorId.equals(player.getUUID());
          }
        } else {
          canEdit = false;
        }
        if (canEdit) {
          ItemStack writableBookStack = new ItemStack(Items.WRITABLE_BOOK);
          ListTag pagesTag = new ListTag();
          // written books can hold styling information that isn't possible in writable books, and thus are saved to be
          // read as components in JSON format. so we have to parse out the plain text.
          BookViewScreen.loadPages(bookStack.getTag(), rawPage -> {
            Component comp = Component.Serializer.fromJsonLenient(rawPage);
            StringBuilder sb = new StringBuilder();
            comp.getContents().visit(text -> {
              sb.append(text);
              return Optional.empty();
            });
            comp.getSiblings().forEach(c -> c.visit(text -> {
              sb.append(text);
              return Optional.empty();
            }));
            pagesTag.add(StringTag.valueOf(sb.toString()));
          });
          writableBookStack.addTagElement("pages", pagesTag);
          player.setItemInHand(otherHand, writableBookStack);
          if (!isCreative && quillStack.isDamageableItem()) {
            quillStack.hurt(1, player.getRandom(), (ServerPlayer) player);
          }
        }
      }
    }
    return InteractionResultHolder.sidedSuccess(quillStack, level.isClientSide);
  }

}
