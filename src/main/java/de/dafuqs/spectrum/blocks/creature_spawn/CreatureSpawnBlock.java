package de.dafuqs.spectrum.blocks.creature_spawn;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CreatureSpawnBlock extends BlockWithEntity {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D);
	
	public CreatureSpawnBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CreatureSpawnBlockEntity(pos, state);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if(blockEntity instanceof CreatureSpawnBlockEntity creatureSpawnBlockEntity) {
			creatureSpawnBlockEntity.advanceHatching(world, pos);
		}
	}
	
}
