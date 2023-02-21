package de.dafuqs.spectrum.blocks.dd_deco;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public interface Dragonjag {

    enum Variant {
        GREEN,
        RED,
        PINK,
        PURPLE,
        BLACK
    }

    Dragonjag.Variant getVariant();

    static boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.getMaterial().isSolid();
    }

}
