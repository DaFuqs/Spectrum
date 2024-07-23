package de.dafuqs.spectrum.blocks.deeper_down;

import com.google.common.collect.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

import java.util.*;

public class OpalSpireBlock extends SpectrumFacingBlock {
	
	public static final Map<Direction.Axis, VoxelShape> SHAPES;
	
	public OpalSpireBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		return this.getDefaultState().with(FACING, direction);
	}
	
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(3) == 0) {
			Direction direction = state.get(FACING);
			double x = (double) pos.getX() + 0.55 - (double) (random.nextFloat() * 0.1F);
			double y = (double) pos.getY() + 0.55 - (double) (random.nextFloat() * 0.1F);
			double z = (double) pos.getZ() + 0.55 - (double) (random.nextFloat() * 0.1F);
			double g = 0.5F - (random.nextFloat() + random.nextFloat()) * 0.5F;
			
			world.addParticle(ParticleTypes.END_ROD, x + (double) direction.getOffsetX() * g, y + (double) direction.getOffsetY() * g, z + (double) direction.getOffsetZ() * g, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005);
		}
		
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(FACING).getAxis());
	}
	
	static {
		var col = ImmutableMap.<Direction.Axis, VoxelShape>builder();
		col.put(Direction.Axis.X, createCuboidShape(0, 3, 3, 16, 13, 13));
		col.put(Direction.Axis.Y, createCuboidShape(3, 0, 3, 13, 16, 13));
		col.put(Direction.Axis.Z, createCuboidShape(3, 3, 0, 13, 13, 16));
		SHAPES = col.build();
	}
}
