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

public interface SpiritVines {
	
	VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	EnumProperty<YieldType> YIELD = EnumProperty.of("yield", YieldType.class);
	
	static ActionResult pick(BlockState blockState, World world, BlockPos blockPos) {
		if (canBeHarvested(blockState)) {
			Block.dropStack(world, blockPos, new ItemStack(getYieldItem(blockState, false), 1));
			float f = MathHelper.nextBetween(world.random, 0.8F, 1.2F);
			world.playSound(null, blockPos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, f);
			world.setBlockState(blockPos, blockState.with(YIELD, YieldType.NONE), 2);
			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}
	
	static boolean canBeHarvested(BlockState state) {
		return state.contains(YIELD) && !state.get(YIELD).equals(YieldType.NONE);
	}
	
	static Item getYieldItem(BlockState blockState, boolean pickStack) {
		Comparable<YieldType> yield = blockState.get(YIELD);
		
		if (yield.equals(YieldType.NORMAL)) {
			if (blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_CYAN_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_MAGENTA_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_YELLOW_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_BLACK_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_WHITE_CATKIN;
			}
		} else if (yield.equals(YieldType.LUCID)) {
			if (blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.LUCID_CYAN_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.LUCID_MAGENTA_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.LUCID_YELLOW_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.LUCID_BLACK_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.LUCID_WHITE_CATKIN;
			}
		} else if (yield.equals(YieldType.NONE) && pickStack) {
			if (blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_CYAN_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_MAGENTA_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_YELLOW_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_BLACK_CATKIN;
			}
			if (blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_HEAD) || blockState.isOf(SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_BODY)) {
				return SpectrumItems.VIBRANT_WHITE_CATKIN;
			}
		}
		return null;
	}
	
	enum YieldType implements StringIdentifiable {
		NONE("none"),
		NORMAL("normal"),
		LUCID("lucid");
		
		private final String name;
		
		YieldType(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		@Override
		public String asString() {
			return this.name;
		}
	}
	
}
