package de.dafuqs.spectrum.blocks.structure;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class InvisibleWallBlock extends AbstractGlassBlock {
	
	public InvisibleWallBlock(Settings settings) {
		super(settings);
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context.isHolding(this.asItem())) {
			return VoxelShapes.fullCube();
		} else {
			return VoxelShapes.empty();
		}
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.fullCube();
	}
	
}
