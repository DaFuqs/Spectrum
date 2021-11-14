package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DecayAwayBlock extends Block {

	private final BlockState conversionBlockState = Blocks.DIRT.getDefaultState();

	public DecayAwayBlock(Settings settings) {
		super(settings);
	}

	public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			world.createAndScheduleBlockTick(pos, state.getBlock(), 4);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);

		// convert all neighboring decay blocks to this
		for(Direction direction : Direction.values()) {
			BlockPos targetBlockPos = pos.offset(direction);
			BlockState currentBlockState = world.getBlockState(targetBlockPos);

			if (Support.hasTag(currentBlockState, SpectrumBlockTags.DECAY)) {
				world.setBlockState(targetBlockPos, this.getDefaultState());
				world.createAndScheduleBlockTick(targetBlockPos, state.getBlock(), 8);
			}
		}

		// and turn this to the leftover block state
		world.setBlockState(pos, conversionBlockState, 3);
	}
}
