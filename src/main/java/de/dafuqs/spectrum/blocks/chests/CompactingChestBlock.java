package de.dafuqs.spectrum.blocks.chests;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

public class CompactingChestBlock extends SpectrumChestBlock {
	
	public static final MapCodec<CompactingChestBlock> CODEC = simpleCodec(CompactingChestBlock::new);
	
	public CompactingChestBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	
	@Override
	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CompactingChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.COMPACTING_CHEST.get(), CompactingChestBlockEntity::tick);
	}
	
	@Override
	public void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				player.openMenu(compactingChestBlockEntity);
			}
		}
	}
	
}
