package de.dafuqs.pigment.blocks.types.budding;

import de.dafuqs.pigment.blocks.PigmentBlocks;
import net.minecraft.block.Block;

public class BuddingMoonstoneBlock extends BuddingPigmentBlock {

    public BuddingMoonstoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getSmall() {
        return PigmentBlocks.SMALL_MOONSTONE_BUD;
    }

    @Override
    protected Block getMedium() {
        return PigmentBlocks.MEDIUM_MOONSTONE_BUD;
    }

    @Override
    protected Block getLarge() {
        return PigmentBlocks.LARGE_MOONSTONE_BUD;
    }

    @Override
    protected Block getCluster() {
        return PigmentBlocks.MOONSTONE_CLUSTER;
    }

}
