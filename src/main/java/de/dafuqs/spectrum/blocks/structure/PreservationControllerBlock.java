package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
					preservationControllerBlockEntity.yeetPlayer(player);
					preservationControllerBlockEntity.toggleParticles();
				} else {
					preservationControllerBlockEntity.openExit();
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
			return checkType(type, SpectrumBlockEntityRegistry.PRESERVATION_CONTROLLER, PreservationControllerBlockEntity::serverTick);
		}
		return null;
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
}
