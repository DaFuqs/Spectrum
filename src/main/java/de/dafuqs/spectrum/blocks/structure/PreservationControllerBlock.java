package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class PreservationControllerBlock extends BlockWithEntity {
	
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	
	public PreservationControllerBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && player.isCreative()) { // for testing and building structures
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PreservationControllerBlockEntity preservationControllerBlockEntity) {
				if (player.isSneaking()) {
					preservationControllerBlockEntity.openExit();
				} else {
					preservationControllerBlockEntity.toggleParticles();
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide().getOpposite();
		if (direction == Direction.UP || direction == Direction.DOWN) { // those do not exist in Properties.HORIZONTAL_FACING
			direction = Direction.NORTH;
		}
		return this.getDefaultState().with(FACING, direction);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationControllerBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return checkType(type, SpectrumBlockEntities.PRESERVATION_CONTROLLER, PreservationControllerBlockEntity::serverTick);
		}
		return null;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
}
