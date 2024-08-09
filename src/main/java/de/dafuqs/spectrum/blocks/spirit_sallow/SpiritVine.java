package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public interface SpiritVine {

	VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	BooleanProperty CRYSTALS = BooleanProperty.of("crystals");

	static ActionResult pick(BlockState blockState, World world, BlockPos blockPos) {
		if (canBeHarvested(blockState)) {
			Block.dropStack(world, blockPos, new ItemStack(getYieldItem(blockState), 1));
			float f = MathHelper.nextBetween(world.random, 0.8F, 1.2F);
			world.playSound(null, blockPos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, f);
			world.setBlockState(blockPos, blockState.with(CRYSTALS, false), 2);
			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}

	static boolean canBeHarvested(BlockState state) {
		return state.contains(CRYSTALS) && state.get(CRYSTALS);
	}
	
	static Item getYieldItem(BlockState blockState) {
		if (blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES) || blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.TOPAZ_SHARD;
		}
		if (blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES) || blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_PLANT)) {
			return Items.AMETHYST_SHARD;
		}
		if (blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES) || blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.CITRINE_SHARD;
		}
		if (blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES) || blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.ONYX_SHARD;
		}
		if (blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES) || blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.MOONSTONE_SHARD;
		}
		return Items.AIR;
	}

}
