package de.dafuqs.spectrum.blocks;

import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public abstract class SpreadableFloraBlock extends TallGrassBlock {

    private final int tries;
	
	public SpreadableFloraBlock(int tries, BlockBehaviour.Properties settings) {
        super(settings);
        this.tries = tries;
    }

    @Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		var tries = Mth.nextInt(random, 0, this.tries);

        for (int i = 0; i < tries; i++) {
			var column = pos.offset(Mth.nextInt(random, -7, 7), 1, Mth.nextInt(random, -7, 7));

            for (int offset = 0; offset < 4; offset++) {
				var plantPos = column.below(offset);
				
				if (mayPlaceOn(world.getBlockState(plantPos), world, plantPos) && world.isEmptyBlock(plantPos.above())) {
					world.setBlockAndUpdate(plantPos.above(), defaultBlockState());
                    break;
                }
            }
        }
    }
}
