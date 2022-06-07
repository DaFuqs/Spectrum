package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DecayAwayBlock extends Block {
	
	private static final EnumProperty<TargetConversion> TARGET_CONVERSION = EnumProperty.of("target_conversion", TargetConversion.class);
	
	public DecayAwayBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(TARGET_CONVERSION, TargetConversion.DEFAULT));
	}
	
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
				world.setBlockState(targetBlockPos, getTargetState(currentBlockState));
				world.createAndScheduleBlockTick(targetBlockPos, state.getBlock(), 8);
			}
		}
		
		// and turn this to the leftover block state
		BlockState currentState = world.getBlockState(pos);
		TargetConversion targetConversion = currentState.get(TARGET_CONVERSION);
		world.setBlockState(pos, targetConversion.getTargetState(), 3);
	}
	
	public BlockState getTargetState(BlockState blockState) {
		if (blockState.getBlock() instanceof DecayBlock) {
			if (blockState.isOf(SpectrumBlocks.RUIN)) {
				if (blockState.get(TerrorBlock.DECAY_STATE) == TerrorBlock.DecayConversion.BEDROCK) {
					return this.getDefaultState().with(TARGET_CONVERSION, TargetConversion.BEDROCK);
				}
			} else if (blockState.isOf(SpectrumBlocks.FAILING)) {
				if (blockState.get(FailingBlock.DECAY_STATE) == FailingBlock.DecayConversion.OBSIDIAN) {
					return this.getDefaultState().with(TARGET_CONVERSION, TargetConversion.OBSIDIAN);
				} else if (blockState.get(FailingBlock.DECAY_STATE) == FailingBlock.DecayConversion.CRYING_OBSIDIAN) {
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
		
		public String toString() {
			return this.name;
		}
		
		public String asString() {
			return this.name;
		}
		
		public BlockState getTargetState() {
			return this.targetState;
		}
	}
	
}
