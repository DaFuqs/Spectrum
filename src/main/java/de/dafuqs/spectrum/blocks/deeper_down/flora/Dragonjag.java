package de.dafuqs.spectrum.blocks.deeper_down.flora;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public interface Dragonjag {

    enum Variant {
        YELLOW,
        RED,
        PINK,
        PURPLE,
        BLACK
    }

    Dragonjag.Variant getVariant();

    static boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOpaqueFullCube(world, pos);
    }

}
