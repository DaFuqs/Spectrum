package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.worldgen.SpectrumConfiguredFeatures;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class CloverBlock extends FernBlock {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 1.0D, 14.0D, 2.0D, 14.0D);
	
	public CloverBlock(Settings settings) {
		super(settings);
	}
	
	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	public AbstractBlock.OffsetType getOffsetType() {
		return OffsetType.XZ;
	}
	
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return super.isFertilizable(world, pos, state, isClient);
	}
	
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}
	
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		SpectrumConfiguredFeatures.CLOVER_PATCH.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
	}
	
}
