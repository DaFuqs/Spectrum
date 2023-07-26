package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.BlockState;

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
