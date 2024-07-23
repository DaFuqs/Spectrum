package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class AshBlock extends Block {
	
	public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 14, 16);
	
	public AshBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.handleFallDamage(fallDistance, 0.2F, world.getDamageSources().fall());
	}
	
}
