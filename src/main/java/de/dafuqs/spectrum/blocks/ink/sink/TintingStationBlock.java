package de.dafuqs.spectrum.blocks.ink.sink;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

import java.util.*;

public class TintingStationBlock extends BaseInkBlock {
	
	public static final MapCodec<TintingStationBlock> CODEC = simpleCodec(TintingStationBlock::new);
	public static final BooleanProperty SHORT = BlockStateProperties.SHORT;
	
	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 13.0D, 15.0D);
	protected static final VoxelShape SHAPE_SHORT = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
	
	public TintingStationBlock(Properties settings) {
		super(settings);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(SHORT, false));
	}
	
	@Override
	public MapCodec<? extends TintingStationBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(SHORT) ? SHAPE_SHORT : SHAPE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(SHORT);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		if(ctx.getPlayer() != null) {
			state = state.setValue(SHORT, ctx.getPlayer().isShiftKeyDown());
		}
		return state;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.tinting_station.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TintingStationBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInkBlockTicker(level, blockEntityType, SpectrumBlockEntities.TINTING_STATION.get());
	}
	
}
