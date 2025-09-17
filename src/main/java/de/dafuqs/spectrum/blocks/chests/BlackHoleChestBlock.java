package de.dafuqs.spectrum.blocks.chests;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public class BlackHoleChestBlock extends SpectrumChestBlock {
	
	public static final MapCodec<BlackHoleChestBlock> CODEC = simpleCodec(BlackHoleChestBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 9.0D, 15.0D);
	
	public BlackHoleChestBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	
	@Override
	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlackHoleChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.BLACK_HOLE_CHEST.get(), BlackHoleChestBlockEntity::tick);
	}
	
	@Override
	public void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BlackHoleChestBlockEntity blackHoleChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				player.openMenu(blackHoleChestBlockEntity);
			}
		}
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> GameEventListener getListener(ServerLevel world, T blockEntity) {
		return blockEntity instanceof BlackHoleChestBlockEntity blackHoleChestBlockEntity ? blackHoleChestBlockEntity.getEventListener() : null;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
