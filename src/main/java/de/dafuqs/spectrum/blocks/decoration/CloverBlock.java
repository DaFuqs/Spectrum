package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

import java.util.*;

public class CloverBlock extends FernBlock {

	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 1.0D, 14.0D, 2.0D, 14.0D);

	public CloverBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		Optional<PlacedFeature> feature = world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE).getOrEmpty(SpectrumPlacedFeatures.CLOVER_PATCH);
		feature.ifPresent(placedFeature -> placedFeature.generate(world, world.getChunkManager().getChunkGenerator(), random, pos));
	}

}
