package de.dafuqs.pigment.blocks.types.budding;

import de.dafuqs.pigment.blocks.PigmentBlocks;
import net.minecraft.block.Block;

public class BuddingOnyxBlock extends BuddingPigmentBlock {

    public BuddingOnyxBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return PigmentBlocks.SMALL_ONYX_BUD;
    }

    @Override
    protected Block getMedium() {
        return PigmentBlocks.MEDIUM_ONYX_BUD;
    }

    @Override
    protected Block getLarge() {
        return PigmentBlocks.LARGE_ONYX_BUD;
    }

    @Override
    protected Block getCluster() {
        return PigmentBlocks.ONYX_CLUSTER;
    }

}
