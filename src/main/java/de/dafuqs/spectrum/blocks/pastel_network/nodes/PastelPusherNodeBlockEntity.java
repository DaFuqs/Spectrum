package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;

public class PastelPusherNodeBlockEntity extends PastelNodeBlockEntity {

    public PastelPusherNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.PUSHER_NODE, blockPos, blockState);
    }

}