package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class AshBlock extends Block {
	
	public static final MapCodec<AshBlock> CODEC = simpleCodec(AshBlock::new);
	
	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 14, 16);
	
	public AshBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends AshBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.causeFallDamage(fallDistance, 0.2F, world.damageSources().fall());
	}
	
}
