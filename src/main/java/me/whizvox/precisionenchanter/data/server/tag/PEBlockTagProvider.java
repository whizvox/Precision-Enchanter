package me.whizvox.precisionenchanter.data.server.tag;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.registry.PEBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PEBlockTagProvider extends BlockTagsProvider {

  public PEBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, PrecisionEnchanter.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
        PEBlocks.ENCHANTERS_WORKBENCH.get(),
        PEBlocks.PRECISION_GRINDSTONE.get()
    );
  }

}
