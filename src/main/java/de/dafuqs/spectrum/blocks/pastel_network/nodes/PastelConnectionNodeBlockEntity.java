package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;

public class PastelConnectionNodeBlockEntity extends PastelNodeBlockEntity {

    public PastelConnectionNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.CONNECTION_NODE, blockPos, blockState);
    }

}