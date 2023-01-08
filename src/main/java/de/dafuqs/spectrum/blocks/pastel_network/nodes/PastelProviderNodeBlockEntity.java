package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;

public class PastelProviderNodeBlockEntity extends PastelNodeBlockEntity {

    public PastelProviderNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.PROVIDER_NODE, blockPos, blockState);
    }

}