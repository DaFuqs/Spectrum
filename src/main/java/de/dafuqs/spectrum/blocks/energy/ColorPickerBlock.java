package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColorPickerBlock extends BlockWithEntity {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
	
	public ColorPickerBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.color_picker.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ColorPickerBlockEntity(pos, state);
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return checkType(type, SpectrumBlockEntities.COLOR_PICKER, ColorPickerBlockEntity::tick);
		}
		return null;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return ActionResult.CONSUME;
		}
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ColorPickerBlockEntity colorPickerBlockEntity) {
			colorPickerBlockEntity.setOwner(player);
			player.openHandledScreen(colorPickerBlockEntity);
		}
	}
	
	@Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
	
	@Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		if(world.getBlockEntity(pos) instanceof ColorPickerBlockEntity blockEntity) {
			int i = 0;
			float f = 0.0f;
			for (int j = 0; j < blockEntity.inventory.size(); ++j) {
				ItemStack itemStack = blockEntity.inventory.get(j);
				if (itemStack.isEmpty()) continue;
				f += (float)itemStack.getCount() / (float)itemStack.getMaxCount();
				++i;
			}
			return MathHelper.floor(f / (float) blockEntity.inventory.size() * 14.0f) + (i > 0 ? 1 : 0);
		}
		
		return 0;
    }
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ColorPickerBlockEntity colorPickerBlockEntity) {
				ItemScatterer.spawn(world, pos, colorPickerBlockEntity);
				world.updateComparators(pos, this);
			}
			
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
}