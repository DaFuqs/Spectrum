package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class InvisibleWallBlock extends TransparentBlock {
	
	public static final MapCodec<InvisibleWallBlock> CODEC = simpleCodec(InvisibleWallBlock::new);
	
	public InvisibleWallBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends InvisibleWallBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context.isHoldingItem(this.asItem())) {
			return Shapes.block();
		} else {
			return Shapes.empty();
		}
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
	
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return 1.0F;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.block();
	}
	
}
