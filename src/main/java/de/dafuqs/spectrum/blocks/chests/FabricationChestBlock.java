package de.dafuqs.spectrum.blocks.chests;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

public class FabricationChestBlock extends SpectrumChestBlock {
	
	public static final MapCodec<FabricationChestBlock> CODEC = simpleCodec(FabricationChestBlock::new);
	
	protected static final VoxelShape CHEST_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	
	public FabricationChestBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FabricationChestBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.FABRICATION_CHEST.get(), FabricationChestBlockEntity::tick);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return CHEST_SHAPE;
	}
	
}
