package me.whizvox.precisionenchanter.data.client;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PEItemModelProvider extends ItemModelProvider {

  public PEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, PrecisionEnchanter.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {

  }

}
