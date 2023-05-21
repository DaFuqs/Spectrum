package de.dafuqs.spectrum.blocks.crystallarieum;

import net.minecraft.block.*;
import net.minecraft.block.piston.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class CrystallarieumGrowableBlock extends Block {
	
	public enum GrowthStage {
		SMALL(3, 4),
		MEDIUM(4, 3),
		LARGE(5, 3),
		CLUSTER(7, 3);
		
		final VoxelShape shape;
		
		GrowthStage(int height, int xzOffset) {
			this.shape = Block.createCuboidShape(xzOffset, 0.0D, xzOffset, (16 - xzOffset), height, (16 - xzOffset));
		}
		
		public VoxelShape getShape() {
			return this.shape;
		}
		
	}
	
	public final GrowthStage growthStage;
	
	public CrystallarieumGrowableBlock(AbstractBlock.Settings settings, GrowthStage growthStage) {
		super(settings);
		this.growthStage = growthStage;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.offset(Direction.DOWN);
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.DOWN);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.growthStage.getShape();
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
}
