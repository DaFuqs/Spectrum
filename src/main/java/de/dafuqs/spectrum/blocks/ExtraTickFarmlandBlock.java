package de.dafuqs.spectrum.blocks;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ExtraTickFarmlandBlock extends FarmlandBlock {
	
	public ExtraTickFarmlandBlock(Settings settings) {
		super(settings);
	}
	
	private static boolean hasCrop(@NotNull BlockView world, @NotNull BlockPos pos) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
	}
	
	/**
	 * If there is a crop block on top of this block: tick it, too
	 * => the crop grows faster
	 */
	public void randomTick(BlockState state, @NotNull ServerWorld world, @NotNull BlockPos pos, Random random) {
		BlockPos topPos = pos.up();
		BlockState topBlockState = world.getBlockState(topPos);
		if (hasCrop(world, pos)) {
			topBlockState.getBlock().randomTick(topBlockState, world, topPos, random);
		}
		
		super.randomTick(state, world, pos, random);
	}
	
}
