package de.dafuqs.spectrum.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public abstract class SpreadableFloraBlock extends FernBlock {

    private final int tries;

    public SpreadableFloraBlock(int tries, Settings settings) {
        super(settings);
        this.tries = tries;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var tries = MathHelper.nextInt(random, 0, this.tries);

        for (int i = 0; i < tries; i++) {
            var column = pos.add(MathHelper.nextInt(random, -7, 7), 1, MathHelper.nextInt(random, -7, 7));

            for (int offset = 0; offset < 4; offset++) {
                var plantPos = column.down(offset);

                if (canPlantOnTop(world.getBlockState(plantPos), world, plantPos) && world.isAir(plantPos.up())) {
                    world.setBlockState(plantPos.up(), getDefaultState());
                    break;
                }
            }
        }
    }
}
