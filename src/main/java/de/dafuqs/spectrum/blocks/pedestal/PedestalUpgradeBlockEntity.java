package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PedestalUpgradeBlockEntity extends BlockEntity {

    public PedestalUpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntityRegistry.PEDESTAL_SPEED_UPGRADE, pos, state);
    }

}
