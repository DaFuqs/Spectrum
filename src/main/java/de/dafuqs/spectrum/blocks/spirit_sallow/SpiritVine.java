package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;

public interface SpiritVine {
	
	VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	BooleanProperty CRYSTALS = BooleanProperty.create("crystals");
	
	static InteractionResult pick(BlockState blockState, Level world, BlockPos blockPos) {
		if (canBeHarvested(blockState)) {
			Block.popResource(world, blockPos, new ItemStack(getYieldItem(blockState), 1));
			float f = Mth.randomBetween(world.random, 0.8F, 1.2F);
			world.playSound(null, blockPos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);
			world.setBlock(blockPos, blockState.setValue(CRYSTALS, false), 2);
			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	static boolean canBeHarvested(BlockState state) {
		return state.hasProperty(CRYSTALS) && state.getValue(CRYSTALS);
	}
	
	static Item getYieldItem(BlockState blockState) {
		if (blockState.is(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES) || blockState.is(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.TOPAZ_SHARD;
		}
		if (blockState.is(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES) || blockState.is(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_PLANT)) {
			return Items.AMETHYST_SHARD;
		}
		if (blockState.is(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES) || blockState.is(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.CITRINE_SHARD;
		}
		if (blockState.is(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES) || blockState.is(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.ONYX_SHARD;
		}
		if (blockState.is(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES) || blockState.is(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_PLANT)) {
			return SpectrumItems.MOONSTONE_SHARD;
		}
		return Items.AIR;
	}

}
