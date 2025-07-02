package de.dafuqs.spectrum.blocks.statues;

import com.mojang.serialization.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GrotesqueBlock extends HorizontalDirectionalBlock {

	private final VoxelShape shape;
	protected final Component tooltipText;
	
	
	public GrotesqueBlock(Properties settings, double width, double height, String tooltipKey) {
		super(settings);
		tooltipText = Component.translatable(tooltipKey).withStyle(ChatFormatting.GRAY);
		var min = (16 - width) / 2;
		var max = width + min;
		shape = Block.box(min, 0, min, max, height, max);
	}

	@Override
	public MapCodec<? extends GrotesqueBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(tooltipText);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return shape;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
