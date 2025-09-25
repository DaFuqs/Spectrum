package de.dafuqs.spectrum.blocks.lava_sponge;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;

public class LavaSpongeBlock extends SpongeBlock {
	
	public static final MapCodec<LavaSpongeBlock> CODEC = simpleCodec(LavaSpongeBlock::new);
	
	public LavaSpongeBlock(Properties settings) {
		super(settings);
	}

//	@Override
//	public MapCodec<? extends LavaSpongeBlock> getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}
	
	@Override
	protected void tryAbsorbWater(Level world, BlockPos pos) {
		if (this.absorbLava(world, pos)) {
			world.setBlock(pos, SpectrumBlocks.WET_LAVA_SPONGE.get().defaultBlockState(), 2);
			world.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	private boolean absorbLava(Level world, BlockPos pos) {
		return BlockPos.breadthFirstTraversal(pos, 6, 65, (currentPos, queuer) -> {
			for (Direction direction : Direction.values()) {
				queuer.accept(currentPos.relative(direction));
			}
		}, (currentPos) -> {
			if (currentPos.equals(pos)) {
				return true;
			} else {
				BlockState blockState = world.getBlockState(currentPos);
				FluidState fluidState = world.getFluidState(currentPos);
				if (!fluidState.is(FluidTags.LAVA)) {
					return false;
				} else {
					Block block = blockState.getBlock();
					if (block instanceof BucketPickup fluidDrainable) {
						if (!fluidDrainable.pickupBlock(null, world, currentPos, blockState).isEmpty()) {
							return true;
						}
					}
					if (blockState.getBlock() instanceof LiquidBlock) {
						world.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
					}
					return true;
				}
			}
		}) > 1;
	}
	
}
