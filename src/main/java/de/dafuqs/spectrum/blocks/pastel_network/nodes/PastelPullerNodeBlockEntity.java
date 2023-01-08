package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;

public class PastelPullerNodeBlockEntity extends PastelNodeBlockEntity {

    public PastelPullerNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.PULLER_NODE, blockPos, blockState);
    }

}