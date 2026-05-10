package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import javax.annotation.*;

public abstract class AbstractParticleSpawnerBlock extends BaseEntityBlock {
	
	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
	
	public AbstractParticleSpawnerBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ParticleSpawnerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide() ? createTickerHelper(type, SpectrumBlockEntities.PARTICLE_SPAWNER.get(), ParticleSpawnerBlockEntity::clientTick) : null;
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ParticleSpawnerBlockEntity particleSpawnerBlockEntity) {
                player.openMenu(particleSpawnerBlockEntity);
            }
            return InteractionResult.CONSUME;
        }
	}
	
	public abstract boolean shouldSpawnParticles(Level world, BlockPos pos);
	
}
