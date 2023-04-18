package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class CloverBlock extends FernBlock {

	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 1.0D, 14.0D, 2.0D, 14.0D);

	public CloverBlock(Settings settings) {
		super(settings);
	}

	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		world.getRegistryManager().get(Registry.CONFIGURED_FEATURE_KEY).get(SpectrumConfiguredFeatures.CLOVER_PATCH).generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
	}

}
