package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class PreservationControllerBlock extends BaseEntityBlock {
	
	public static final MapCodec<PreservationControllerBlock> CODEC = simpleCodec(PreservationControllerBlock::new);
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public PreservationControllerBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends PreservationControllerBlock> codec() {
		return CODEC;
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!world.isClientSide && player.isCreative()) { // for testing and building structures
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PreservationControllerBlockEntity preservationControllerBlockEntity) {
				if (player.isShiftKeyDown()) {
					preservationControllerBlockEntity.openExit();
				} else {
					preservationControllerBlockEntity.toggleParticles();
				}
			}
		}
		return super.useWithoutItem(state, world, pos, player, hit);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction direction = ctx.getClickedFace().getOpposite();
		if (direction == Direction.UP || direction == Direction.DOWN) { // those do not exist in Properties.HORIZONTAL_FACING
			direction = Direction.NORTH;
		}
		return this.defaultBlockState().setValue(FACING, direction);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationControllerBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? null : createTickerHelper(type, SpectrumBlockEntities.PRESERVATION_CONTROLLER.get(), PreservationControllerBlockEntity::serverTick);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
}
