package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

public class PastelConnectionNodeBlockEntity extends PastelNodeBlockEntity {

    public PastelConnectionNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.CONNECTION_NODE, blockPos, blockState);
    }

    @Override
    public PastelNodeType getNodeType() {
        return PastelNodeType.CONNECTION;
    }

    public @Nullable Storage<ItemVariant> getConnectedStorage() {
        return null;
    }

}