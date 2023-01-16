package me.whizvox.precisionenchanter.common.network.message;

import me.whizvox.precisionenchanter.common.network.MessageHandler;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipe;
import me.whizvox.precisionenchanter.common.recipe.EnchantmentRecipeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record SyncEnchantmentRecipesMessage(List<EnchantmentRecipe> recipes) {

  public static final MessageHandler<SyncEnchantmentRecipesMessage> HANDLER = new MessageHandler<>() {

    @Override
    public Class<SyncEnchantmentRecipesMessage> getType() {
      return SyncEnchantmentRecipesMessage.class;
    }

    @Override
    public void encode(SyncEnchantmentRecipesMessage msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.recipes.size());
      msg.recipes.forEach(recipe -> recipe.toNetwork(buf));
    }

    @Override
    public SyncEnchantmentRecipesMessage decode(FriendlyByteBuf buf) {
      int numRecipes = buf.readInt();
      List<EnchantmentRecipe> recipes = new ArrayList<>(numRecipes);
      for (int i = 0; i < numRecipes; i++) {
        recipes.add(EnchantmentRecipe.fromNetwork(buf).immutable());
      }
      return new SyncEnchantmentRecipesMessage(Collections.unmodifiableList(recipes));
    }

    @Override
    public void handle(NetworkEvent.Context ctx, SyncEnchantmentRecipesMessage msg) {
      EnchantmentRecipeManager.INSTANCE.sync(msg.recipes);
    }

  };

}
