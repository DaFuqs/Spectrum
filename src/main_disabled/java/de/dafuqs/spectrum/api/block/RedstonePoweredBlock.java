package de.dafuqs.spectrum.api.block;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.properties.*;

public interface RedstonePoweredBlock {
	
	default boolean checkGettingPowered(Level world, BlockPos pos) {
		Direction[] var4 = Direction.values();
		int var5 = var4.length;
		
		int var6;
		for (var6 = 0; var6 < var5; ++var6) {
			Direction direction = var4[var6];
			if (world.hasSignal(pos.relative(direction), direction)) {
				return true;
			}
		}
		
		if (world.hasSignal(pos, Direction.DOWN)) {
			return true;
		} else {
			BlockPos blockPos = pos.above();
			Direction[] var10 = Direction.values();
			var6 = var10.length;
			
			for (int var11 = 0; var11 < var6; ++var11) {
				Direction direction2 = var10[var11];
				if (direction2 != Direction.DOWN && world.hasSignal(blockPos.relative(direction2), direction2)) {
					return true;
				}
			}
			return false;
		}
	}
	
	default void power(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(BlockStateProperties.POWERED, true));
	}
	
	default void unPower(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(BlockStateProperties.POWERED, false));
	}
	
}
