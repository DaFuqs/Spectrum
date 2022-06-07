package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

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
		
		private YieldType(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		public String asString() {
			return this.name;
		}
	}
	
}
