package me.whizvox.precisionenchanter.data.server.tag;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class PEBlockTagProvider extends BlockTagsProvider {

  public PEBlockTagProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
    super(gen, PrecisionEnchanter.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
        PEBlocks.ENCHANTERS_WORKBENCH.get(),
        PEBlocks.PRECISION_GRINDSTONE.get()
    );
  }

}
