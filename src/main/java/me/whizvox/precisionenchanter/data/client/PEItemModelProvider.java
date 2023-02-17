package me.whizvox.precisionenchanter.data.client;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.registry.PEItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PEItemModelProvider extends ItemModelProvider {

  public PEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, PrecisionEnchanter.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    basicItem(PEItems.QUILL.get());
    basicItem(PEItems.ENCHANTED_QUILL.get());
  }

}
