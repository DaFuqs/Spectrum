package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.dimension.*;
import org.jetbrains.annotations.*;

public class DecayAwayBlock extends Block {
	
	private static final EnumProperty<TargetConversion> TARGET_CONVERSION = EnumProperty.of("target_conversion", TargetConversion.class);
	
	public DecayAwayBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(TARGET_CONVERSION, TargetConversion.DEFAULT));
	}
	
	@Override
	public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			world.createAndScheduleBlockTick(pos, state.getBlock(), 4);
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(TARGET_CONVERSION);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		
		// convert all neighboring decay blocks to this
		for (Direction direction : Direction.values()) {
			BlockPos targetBlockPos = pos.offset(direction);
			BlockState currentBlockState = world.getBlockState(targetBlockPos);
			
			if (currentBlockState.isIn(SpectrumBlockTags.DECAY_AWAY_CURABLES)) {
				world.setBlockState(targetBlockPos, getTargetStateForCurable(currentBlockState));
				world.createAndScheduleBlockTick(targetBlockPos, state.getBlock(), 8);
			}
		}
		
		// and turn this to the leftover block state
		BlockState currentState = world.getBlockState(pos);
		TargetConversion targetConversion = currentState.get(TARGET_CONVERSION);
		world.setBlockState(pos, targetConversion.getTargetState(world), 3);
	}
	
	public BlockState getTargetStateForCurable(BlockState blockState) {
		if (blockState.getBlock() instanceof DecayBlock) {
			if (blockState.isOf(SpectrumBlocks.RUIN) || blockState.isOf(SpectrumBlocks.FORFEITURE)) {
				if (blockState.get(ForfeitureBlock.CONVERSION) == DecayBlock.Conversion.DEFAULT) {
					return this.getDefaultState().with(TARGET_CONVERSION, TargetConversion.BEDROCK);
				}
			} else if (blockState.isOf(SpectrumBlocks.FAILING)) {
				if (blockState.get(FailingBlock.CONVERSION) == DecayBlock.Conversion.DEFAULT) {
					return this.getDefaultState().with(TARGET_CONVERSION, TargetConversion.OBSIDIAN);
				} else if (blockState.get(FailingBlock.CONVERSION) == DecayBlock.Conversion.SPECIAL) {
					return this.getDefaultState().with(TARGET_CONVERSION, TargetConversion.CRYING_OBSIDIAN);
				}
			}
		}
		return this.getDefaultState();
	}
	
	public enum TargetConversion implements StringIdentifiable {
		DEFAULT("default", Blocks.DIRT.getDefaultState()),
		BEDROCK("bedrock", Blocks.BEDROCK.getDefaultState()),
		OBSIDIAN("obsidian", Blocks.OBSIDIAN.getDefaultState()),
		CRYING_OBSIDIAN("crying_obsidian", Blocks.CRYING_OBSIDIAN.getDefaultState());
		
		private final String name;
		private final BlockState targetState;
		
		TargetConversion(String name, BlockState targetState) {
			this.name = name;
			this.targetState = targetState;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		@Override
		public String asString() {
			return this.name;
		}
		
		public BlockState getTargetState(World world) {
			if (this == DEFAULT) {
				Identifier identifier = world.getDimension().effects();
				if (DimensionTypes.THE_NETHER_ID.equals(identifier)) {
					return Blocks.NETHERRACK.getDefaultState();
				} else if (DimensionTypes.THE_END_ID.equals(identifier)) {
					return Blocks.END_STONE.getDefaultState();
				}
				return Blocks.DIRT.getDefaultState();
			}
			return this.targetState;
		}
	}
	
}
