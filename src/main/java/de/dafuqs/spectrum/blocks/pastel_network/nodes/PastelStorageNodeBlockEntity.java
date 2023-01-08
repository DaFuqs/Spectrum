package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;

public class PastelStorageNodeBlockEntity extends PastelNodeBlockEntity {

    public PastelStorageNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.STORAGE_NODE, blockPos, blockState);
    }

    @Override
    public PastelNodeType getNodeType() {
        return PastelNodeType.STORAGE;
    }

}