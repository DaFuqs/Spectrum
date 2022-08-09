package de.dafuqs.spectrum.blocks.crystallarieum;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

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
	
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.offset(Direction.DOWN);
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.DOWN);
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.growthStage.getShape();
	}
	
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
}
