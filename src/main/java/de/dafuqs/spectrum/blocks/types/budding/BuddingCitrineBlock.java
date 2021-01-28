package de.dafuqs.spectrum.blocks.types.budding;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.minecraft.block.Block;

public class BuddingCitrineBlock extends BuddingSpectrumBlock {

    public BuddingCitrineBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return SpectrumBlocks.SMALL_CITRINE_BUD;
    }

    @Override
    protected Block getMedium() {
        return SpectrumBlocks.MEDIUM_CITRINE_BUD;
    }

    @Override
    protected Block getLarge() {
        return SpectrumBlocks.LARGE_CITRINE_BUD;
    }

    @Override
    protected Block getCluster() {
        return SpectrumBlocks.CITRINE_CLUSTER;
    }

}
