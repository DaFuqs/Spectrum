package de.dafuqs.spectrum.blocks.energy;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColorPickerBlock extends HorizontalDirectionalBlock implements EntityBlock {
	
	public static final MapCodec<ColorPickerBlock> CODEC = simpleCodec(ColorPickerBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
	
	public ColorPickerBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends ColorPickerBlock> codec() {
		return CODEC;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.color_picker.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ColorPickerBlockEntity(pos, state);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? null : Support.checkType(type, SpectrumBlockEntities.COLOR_PICKER.get(), ColorPickerBlockEntity::tick);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return InteractionResult.CONSUME;
		}
	}
	
	protected void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ColorPickerBlockEntity colorPickerBlockEntity) {
			colorPickerBlockEntity.setOwner(player);
			player.openMenu(colorPickerBlockEntity);
		}
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof ColorPickerBlockEntity blockEntity) {
			int i = 0;
			float f = 0.0f;
			for (int j = 0; j < blockEntity.inventory.size(); ++j) {
				ItemStack itemStack = blockEntity.inventory.get(j);
				if (itemStack.isEmpty()) continue;
				f += (float) itemStack.getCount() / (float) itemStack.getMaxStackSize();
				++i;
			}
			return Mth.floor(f / (float) blockEntity.inventory.size() * 14.0f) + (i > 0 ? 1 : 0);
		}
		
		return 0;
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Containers.dropContentsOnDestroy(state, newState, world, pos);
		super.onRemove(state, world, pos, newState, moved);
	}
	
}
