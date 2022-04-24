package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.helpers.TimeHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class JadeVinesBlock extends BlockWithEntity {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public enum JadeVinesBlockPart implements StringIdentifiable {
		UPPER,
		CENTER,
		LOWER;
		
		@Contract(pure = true)
		public @NotNull String toString() {
			return this.asString();
		}
		
		@Contract(pure = true)
		public @NotNull String asString() {
			return this == UPPER ? "upper" : this == CENTER ? "center" : "lower";
		}
		
		public static @Nullable BlockState getDownwardsState(BlockState blockState) {
			JadeVinesBlockPart part = blockState.get(PART);
			if(part == UPPER) {
				return blockState.with(PART, CENTER);
			} else if(part == CENTER) {
				return blockState.with(PART, LOWER);
			}
			return null;
		}
	}
	
	public enum JadeVinesGrowthStage {
		DEAD,
		LEAVES,
		PETALS,
		BLOOM;
		
		public static JadeVinesGrowthStage fromAge(int age) {
			if(age == 0) {
				return DEAD;
			} else if(age == Properties.AGE_7_MAX) {
				return BLOOM;
			} else if(age > 2) {
				return PETALS;
			} else {
				return LEAVES;
			}
		}
		
		public static boolean fullyGrown(int age) {
			return age == Properties.AGE_7_MAX;
		}
		
		public static boolean dead(int age) {
			return age == 0;
		}
		
	}
	
	public static final EnumProperty<JadeVinesBlockPart> PART = EnumProperty.of("part", JadeVinesBlockPart.class);
	public static final IntProperty AGE = Properties.AGE_7;
	
	public JadeVinesBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(PART, JadeVinesBlockPart.UPPER).with(AGE, 1));
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if(oldState.getBlock() instanceof FenceBlock) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
				jadeVinesBlockEntity.setFenceBlockState(oldState.getBlock().getDefaultState());
			}
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
			world.setBlockState(pos, jadeVinesBlockEntity.getFenceBlockState());
		}
	}
	
	@Override
	public boolean canPlaceAt(@NotNull BlockState state, WorldView world, BlockPos pos) {
		return state.getBlock() instanceof FenceBlock;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		
		int age = state.get(AGE);
		JadeVinesGrowthStage stage = JadeVinesGrowthStage.fromAge(age);
		if(stage != JadeVinesGrowthStage.DEAD) {
			// die in sunlight
			if(doesDie(world, pos)) {
				die(state, world, pos);
				world.playSound(null, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
			} else {
				if(tryGrowDownwards(state, world, pos)) {
					world.playSound(null, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				} else if(!JadeVinesGrowthStage.fullyGrown(age) && canGrow(world, pos)) {
					grow(state, world, pos);
					world.playSound(null, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				}
			}
		}
	}
	
	public static boolean doesDie(@NotNull World world, @NotNull BlockPos blockPos) {
		return world.getLightLevel(LightType.SKY, blockPos) > 8 && TimeHelper.isBrightSunlight(world);
	}
	
	/**
	 * Set all 3 blocks (up and down) to dead
	 */
	public static void die(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		JadeVinesBlockPart jadeVinesBlockPart = blockState.get(PART);
		if(jadeVinesBlockPart == JadeVinesBlockPart.UPPER) {
			setDeadIf(world, blockPos.down(), JadeVinesBlockPart.CENTER);
			setDeadIf(world, blockPos.down(2), JadeVinesBlockPart.LOWER);
		} else if(jadeVinesBlockPart == JadeVinesBlockPart.CENTER) {
			setDeadIf(world, blockPos.up(), JadeVinesBlockPart.UPPER);
			setDeadIf(world, blockPos.down(), JadeVinesBlockPart.LOWER);
		} else {
			setDeadIf(world, blockPos.up(2), JadeVinesBlockPart.UPPER);
			setDeadIf(world, blockPos.up(), JadeVinesBlockPart.CENTER);
		}
	}
	
	protected static void setDeadIf(@NotNull World world, BlockPos blockPos, JadeVinesBlockPart part) {
		BlockState state = world.getBlockState(blockPos);
		if(state.getBlock() instanceof JadeVinesBlock && state.get(PART) == part) {
			world.setBlockState(blockPos, state.with(AGE, 0));
		}
	}
	
	/**
	 * Grow all 3 blocks (up + down)
	 */
	public static void grow(BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		int age = blockState.get(AGE);
		if(age != 0 && age != Properties.AGE_7_MAX) {
			age += 1;
			JadeVinesBlockPart jadeVinesBlockPart = blockState.get(PART);
			if (jadeVinesBlockPart == JadeVinesBlockPart.UPPER) {
				growIf(world, blockPos, JadeVinesBlockPart.UPPER, age);
				growIf(world, blockPos.down(), JadeVinesBlockPart.CENTER, age);
				growIf(world, blockPos.down(2), JadeVinesBlockPart.LOWER, age);
			} else if (jadeVinesBlockPart == JadeVinesBlockPart.CENTER) {
				growIf(world, blockPos.up(), JadeVinesBlockPart.UPPER, age);
				growIf(world, blockPos, JadeVinesBlockPart.CENTER, age);
				growIf(world, blockPos.down(), JadeVinesBlockPart.LOWER, age);
			} else {
				growIf(world, blockPos.up(2), JadeVinesBlockPart.UPPER, age);
				growIf(world, blockPos.up(), JadeVinesBlockPart.CENTER, age);
				growIf(world, blockPos, JadeVinesBlockPart.LOWER, age);
			}
		}
	}
	
	protected static void growIf(@NotNull World world, BlockPos blockPos, JadeVinesBlockPart part, int newAge) {
		BlockState state = world.getBlockState(blockPos);
		if(state.getBlock() instanceof JadeVinesBlock && state.get(PART) == part) {
			world.setBlockState(blockPos, state.with(AGE, 0));
		}
	}
	
	public static boolean canGrow(@NotNull World world, @NotNull BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
			if (world.getLightLevel(LightType.SKY, blockPos) > 8 && jadeVinesBlockEntity.isLaterNight(world)) {
				jadeVinesBlockEntity.setLastGrownTime(world.getTime());
				return true;
			}
		}
		return false;
	}
	
	public static boolean tryGrowDownwards(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		BlockState downwardsState = JadeVinesBlockPart.getDownwardsState(blockState);
		if(downwardsState != null && world.getBlockState(blockPos.down()) != downwardsState) {
			world.setBlockState(blockPos.down(), downwardsState);
			return true;
		}
		return false;
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(PART, AGE);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new JadeVinesBlockEntity(pos, state);
	}
	
}
