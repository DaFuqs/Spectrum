package de.dafuqs.spectrum.blocks.upgrade;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class UpgradeBlockEntity extends BlockEntity {
	
	public UpgradeBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.UPGRADE_BLOCK, pos, state);
	}
	
}
