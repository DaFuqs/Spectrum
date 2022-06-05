package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class SuckingChestBlock extends SpectrumChestBlock {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D);
	
	public SuckingChestBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SuckingChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SpectrumBlockEntityRegistry.SUCKING_CHEST, SuckingChestBlockEntity::tick);
	}
	
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SuckingChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			}
		}
	}
	
	@Nullable
	public <T extends BlockEntity> GameEventListener getGameEventListener(World world, T blockEntity) {
		return blockEntity instanceof SuckingChestBlockEntity suckingChestBlockEntity ? suckingChestBlockEntity.getEventListener() : null;
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
}
