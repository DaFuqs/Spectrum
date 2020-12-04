package de.dafuqs.spectrum.blocks.BuddingBlocks;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.minecraft.block.Block;

public class BuddingOnyxBlock extends BuddingSpectrumBlock {

    public BuddingOnyxBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return SpectrumBlocks.SMALL_ONYX_BUD;
    }

    @Override
    protected Block getMedium() {
        return SpectrumBlocks.MEDIUM_ONYX_BUD;
    }

    @Override
    protected Block getLarge() {
        return SpectrumBlocks.LARGE_ONYX_BUD;
    }

    @Override
    protected Block getCluster() {
        return SpectrumBlocks.ONYX_CLUSTER;
    }

}
