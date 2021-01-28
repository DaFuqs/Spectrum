package de.dafuqs.spectrum.blocks.types.budding;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.minecraft.block.Block;

public class BuddingMoonstoneBlock extends BuddingSpectrumBlock {

    public BuddingMoonstoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return SpectrumBlocks.SMALL_MOONSTONE_BUD;
    }

    @Override
    protected Block getMedium() {
        return SpectrumBlocks.MEDIUM_MOONSTONE_BUD;
    }

    @Override
    protected Block getLarge() {
        return SpectrumBlocks.LARGE_MOONSTONE_BUD;
    }

    @Override
    protected Block getCluster() {
        return SpectrumBlocks.MOONSTONE_CLUSTER;
    }

}
