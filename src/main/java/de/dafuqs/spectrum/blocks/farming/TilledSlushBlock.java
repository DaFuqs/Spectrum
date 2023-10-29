package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

public class TilledSlushBlock extends ImmutableFarmlandBlock {
    public TilledSlushBlock(Settings settings, BlockState bareState) {
        super(settings, bareState);
        this.setDefaultState(getDefaultState().with(MOISTURE, 7));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    /**
     * Zoinked
     */
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos topPos = pos.up();
        BlockState topBlockState = world.getBlockState(topPos);
        if (hasCrop(world, pos)) {
            topBlockState.getBlock().randomTick(topBlockState, world, topPos, random);
        }

        super.randomTick(state, world, pos, random);
    }

    private boolean hasCrop(@NotNull BlockView world, @NotNull BlockPos pos) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
    }
}
