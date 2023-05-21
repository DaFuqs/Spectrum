package de.dafuqs.spectrum.blocks.upgrade;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;

public class UpgradeBlockEntity extends BlockEntity {
	
	public UpgradeBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.UPGRADE_BLOCK, pos, state);
	}
	
}
