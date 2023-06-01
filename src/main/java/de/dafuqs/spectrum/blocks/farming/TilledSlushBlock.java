package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class TilledSlushBlock extends ImmutableFarmlandBlock{
    public TilledSlushBlock(Settings settings, BlockState bareState) {
        super(settings, bareState);
        this.setDefaultState(getDefaultState().with(MOISTURE, 7));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }
}
