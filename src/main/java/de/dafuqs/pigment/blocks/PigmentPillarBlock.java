package de.dafuqs.pigment.blocks;

import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.Direction;

public class PigmentPillarBlock extends PillarBlock {

    public PigmentPillarBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(AXIS, Direction.Axis.Y));
    }


}
