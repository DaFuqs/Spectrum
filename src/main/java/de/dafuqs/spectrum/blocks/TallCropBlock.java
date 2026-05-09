package de.dafuqs.spectrum.blocks;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;
import javax.annotation.*;

/**
 * A crop block that is two blocks tall.
 * This class is fully usable on its own, but it is recommended to extend it.
 */
public class TallCropBlock extends CropBlock {
	public static final MapCodec<TallCropBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			Codec.INT.fieldOf("last_single_block_age").forGetter(TallCropBlock::getLastSingleBlockAge)
	).apply(instance, TallCropBlock::new));
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	public final int lastSingleBlockAge;
	
	/**
	 * @param lastSingleBlockAge The highest age for which this block is one block tall.
	 */
	public TallCropBlock(Properties settings, int lastSingleBlockAge) {
		super(settings);
		this.lastSingleBlockAge = lastSingleBlockAge;
	}
	
	@Override
	public MapCodec<? extends TallCropBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.tryGrow(state, world, pos, random, 25F);
    }
	
	@Override
	public void growCrops(Level world, BlockPos pos, BlockState state) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			pos = pos.below();
			state = world.getBlockState(pos);
		}
		if (!state.is(this)) {
			return;
		}
		int newAge = this.getAge(state) + this.getBonemealAgeIncrease(world);
		int maxAge = this.getMaxAge();
		if (newAge > maxAge) {
			newAge = maxAge;
		}
		
		if (newAge > this.lastSingleBlockAge && canGrowUp(world, pos, state, newAge)) {
			world.setBlock(pos, this.getStateForAge(newAge), Block.UPDATE_CLIENTS);
			world.setBlock(pos.above(), this.withAgeAndHalf(newAge, DoubleBlockHalf.UPPER), Block.UPDATE_CLIENTS);
		} else {
			world.setBlock(pos, this.getStateForAge(Math.min(newAge, lastSingleBlockAge)), Block.UPDATE_CLIENTS);
		}
	}
	
	private boolean canGrowUp(Level world, BlockPos pos, BlockState state, int age) {
		return world.getBlockState(pos.above()).is(this) || world.getBlockState(pos.above()).canBeReplaced();
	}
	
	/**
	 * Tries to grow the crop. Call me in randomTick().
	 * Will not do anything if the block state is upper.
	 *
	 * @param upperBound The inverse of the probability of the crop growing with zero moisture.
	 *                   <br>E.g. If upperBound is 25F, the crop with no moisture will grow about 1 in every 25 attempts.
	 *                   The more moisture, the more likely.
	 */
	@SuppressWarnings("SameParameterValue")
	protected void tryGrow(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, float upperBound) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return;
		
		if (world.getRawBrightness(pos, 0) >= 9) {
			int age = this.getAge(state);
			if (age < this.getMaxAge()) {
				float moisture = getGrowthSpeed(state, world, pos);
				// More likely if there's more moisture
				if (random.nextInt((int) (upperBound / moisture) + 1) == 0) {
					if (age >= Block.UPDATE_CLIENTS) {
						if (world.getBlockState(pos.above()).is(this) || world.getBlockState(pos.above()).canBeReplaced()) {
							world.setBlock(pos, this.getStateForAge(age + 1), Block.UPDATE_CLIENTS);
							world.setBlock(pos.above(), this.withAgeAndHalf(age + 1, DoubleBlockHalf.UPPER), Block.UPDATE_CLIENTS);
						}
					} else {
						world.setBlock(pos, this.getStateForAge(age + 1), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
    }

    /**
     * Returns the bottom block state for the given age.
     */
    @Override
	public BlockState getStateForAge(int age) {
        return this.withAgeAndHalf(age, DoubleBlockHalf.LOWER);
    }
    
    public BlockState withAgeAndHalf(int age, DoubleBlockHalf half) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), age).setValue(HALF, half);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HALF).add(AGE);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
			BlockPos blockPos = pos.below();
			return this.mayPlaceOn(world.getBlockState(blockPos), world, blockPos);
		} else {
			BlockState blockState = world.getBlockState(pos.below());
			return blockState.is(this) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER && blockState.getValue(AGE) > this.lastSingleBlockAge;
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			if (state.getValue(AGE) <= this.lastSingleBlockAge) {
				return super.getShape(state, world, pos, context);
			} else {
				// Fill in the bottom block if the plant is two-tall
				return Block.box(0, 0, 0, 16, 16, 16);
			}
		} else {
			return super.getShape(this.getStateForAge(state.getValue(AGE) - this.lastSingleBlockAge - 1), world, pos, context);
		}
	}
	
	// below code is (mostly) copied from TallPlantBlock
	
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
			return (state.getValue(AGE) <= lastSingleBlockAge || neighborState.is(this) && neighborState.getValue(HALF) != doubleBlockHalf) ? state : Blocks.AIR.defaultBlockState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
		}
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos blockPos = ctx.getClickedPos();
		Level world = ctx.getLevel();
		return blockPos.getY() < world.getMaxBuildHeight() - 1 && world.getBlockState(blockPos.above()).canBeReplaced(ctx) ? this.withAgeAndHalf(0, DoubleBlockHalf.LOWER) : null;
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		// Place the other half
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			world.setBlock(pos.below(), this.withAgeAndHalf(state.getValue(AGE), DoubleBlockHalf.LOWER), Block.UPDATE_ALL);
		} else {
			if (state.getValue(AGE) > this.lastSingleBlockAge) {
				world.setBlock(pos.above(), this.withAgeAndHalf(state.getValue(AGE), DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
			}
		}
	}
	
	protected static void breakTheOtherHalf(Level world, BlockPos pos, BlockState state, Player player) {
		if (!player.isCreative()) {
			dropResources(state, world, pos, null, player, player.getMainHandItem());
		}
		
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockPos downPos = pos.below();
			BlockState blockState = world.getBlockState(downPos);
			if (blockState.is(state.getBlock()) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
				breakTheOtherHalf(world, downPos, state, player, downPos, blockState);
			}
		} else {
			BlockPos upPos = pos.above();
			BlockState blockState = world.getBlockState(upPos);
			if (blockState.is(state.getBlock()) && blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
				breakTheOtherHalf(world, pos, state, player, upPos, blockState);
			}
        }
    }
	
	private static void breakTheOtherHalf(Level world, BlockPos pos, BlockState state, Player player, BlockPos upPos, BlockState blockState) {
		BlockState blockState2 = blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
		world.setBlock(upPos, blockState2, Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
		world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, upPos, Block.getId(blockState));
	}
	
	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide) {
			breakTheOtherHalf(world, pos, state, player);
		}
		
		return super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(world, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, stack);
	}
	
	public int getLastSingleBlockAge() {
		return lastSingleBlockAge;
	}
}
