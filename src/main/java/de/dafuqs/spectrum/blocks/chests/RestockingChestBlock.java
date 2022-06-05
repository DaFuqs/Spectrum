package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
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
import org.jetbrains.annotations.Nullable;

public class RestockingChestBlock extends SpectrumChestBlock {
	
	protected static final VoxelShape CHEST_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D);
	
	public RestockingChestBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RestockingChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SpectrumBlockEntityRegistry.RESTOCKING_CHEST, RestockingChestBlockEntity::tick);
	}
	
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof RestockingChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			}
		}
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return CHEST_SHAPE;
	}
	
}
