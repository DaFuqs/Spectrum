package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class CompactingChestBlock extends SpectrumChestBlock {
	
	public CompactingChestBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CompactingChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SpectrumBlockEntities.COMPACTING_CHEST, CompactingChestBlockEntity::tick);
	}
	
	@Override
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				player.openHandledScreen(compactingChestBlockEntity);
			}
		}
	}
	
}
