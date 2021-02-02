package de.dafuqs.pigment.blocks.types.budding;

import de.dafuqs.pigment.blocks.PigmentBlocks;
import net.minecraft.block.Block;

public class BuddingTopazBlock extends BuddingPigmentBlock {

    public BuddingTopazBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return PigmentBlocks.SMALL_TOPAZ_BUD;
    }

    @Override
    protected Block getMedium() {
        return PigmentBlocks.MEDIUM_TOPAZ_BUD;
    }

    @Override
    protected Block getLarge() {
        return PigmentBlocks.LARGE_TOPAZ_BUD;
    }

    @Override
    protected Block getCluster() {
        return PigmentBlocks.TOPAZ_CLUSTER;
    }

}
