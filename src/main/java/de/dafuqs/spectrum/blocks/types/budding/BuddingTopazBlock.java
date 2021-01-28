package de.dafuqs.spectrum.blocks.types.budding;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.minecraft.block.Block;

public class BuddingTopazBlock extends BuddingSpectrumBlock {

    public BuddingTopazBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return SpectrumBlocks.SMALL_TOPAZ_BUD;
    }

    @Override
    protected Block getMedium() {
        return SpectrumBlocks.MEDIUM_TOPAZ_BUD;
    }

    @Override
    protected Block getLarge() {
        return SpectrumBlocks.LARGE_TOPAZ_BUD;
    }

    @Override
    protected Block getCluster() {
        return SpectrumBlocks.TOPAZ_CLUSTER;
    }

}
