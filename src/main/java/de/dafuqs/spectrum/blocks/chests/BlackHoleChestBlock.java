package de.dafuqs.spectrum.blocks.chests;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

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
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlackHoleChestBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.BLACK_HOLE_CHEST.get(), BlackHoleChestBlockEntity::tick);
	}

	@Override
	public <T extends BlockEntity> @Nullable GameEventListener getListener(ServerLevel world, T blockEntity) {
		return blockEntity instanceof BlackHoleChestBlockEntity blackHoleChestBlockEntity ? blackHoleChestBlockEntity.getEventListener() : null;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
