package de.dafuqs.pigment.blocks.budding;

import de.dafuqs.pigment.PigmentBlocks;
import net.minecraft.block.Block;

public class BuddingCitrineBlock extends BuddingPigmentBlock {

    public BuddingCitrineBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return PigmentBlocks.SMALL_CITRINE_BUD;
    }

    @Override
    protected Block getMedium() {
        return PigmentBlocks.MEDIUM_CITRINE_BUD;
    }

    @Override
    protected Block getLarge() {
        return PigmentBlocks.LARGE_CITRINE_BUD;
    }

    @Override
    protected Block getCluster() {
        return PigmentBlocks.CITRINE_CLUSTER;
    }

}
