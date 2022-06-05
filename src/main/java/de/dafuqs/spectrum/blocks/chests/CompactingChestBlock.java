package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CompactingChestBlock extends SpectrumChestBlock {
	
	public CompactingChestBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CompactingChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SpectrumBlockEntityRegistry.COMPACTING_CHEST, CompactingChestBlockEntity::tick);
	}
	
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				player.openHandledScreen(compactingChestBlockEntity);
			}
		}
	}
	
}
