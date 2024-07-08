package de.dafuqs.spectrum.blocks.starfield;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class StarfieldBlockEntity extends BlockEntity {

    public StarfieldBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntities.STARFIELD, pos, state);
    }

}
