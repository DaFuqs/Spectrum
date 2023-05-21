package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.screen.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.event.listener.*;
import org.jetbrains.annotations.*;

public class BlackHoleChestBlock extends SpectrumChestBlock {

	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D);

	public BlackHoleChestBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BlackHoleChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SpectrumBlockEntities.BLACK_HOLE_CHEST, BlackHoleChestBlockEntity::tick);
	}
	
	@Override
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BlackHoleChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			}
		}
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
		return blockEntity instanceof BlackHoleChestBlockEntity blackHoleChestBlockEntity ? blackHoleChestBlockEntity.getEventListener() : null;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
}
