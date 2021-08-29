package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface RedstonePoweredBlock {

    public default boolean checkGettingPowered(World world, BlockPos pos) {
        Direction[] var4 = Direction.values();
        int var5 = var4.length;

        int var6;
        for(var6 = 0; var6 < var5; ++var6) {
            Direction direction = var4[var6];
            if (world.isEmittingRedstonePower(pos.offset(direction), direction)) {
                return true;
            }
        }

        if (world.isEmittingRedstonePower(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockPos = pos.up();
            Direction[] var10 = Direction.values();
            var6 = var10.length;

            for(int var11 = 0; var11 < var6; ++var11) {
                Direction direction2 = var10[var11];
                if (direction2 != Direction.DOWN && world.isEmittingRedstonePower(blockPos.offset(direction2), direction2)) {
                    return true;
                }
            }
            return false;
        }
    }

    public default void power(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(PedestalBlock.POWERED, true));
    }

    public default void unPower(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(PedestalBlock.POWERED, false));
    }

}
