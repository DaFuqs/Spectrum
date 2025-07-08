package de.dafuqs.spectrum.blocks.upgrade;

import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class UpgradeBlockEntity extends BlockEntity {
	
	public UpgradeBlockEntity(BlockPos pos, BlockState state) {
		// TODO: port
		//super(SpectrumBlockEntities.UPGRADE_BLOCK, pos, state);
		super(BlockEntityType.BRUSHABLE_BLOCK, pos, state);
	}
	
}
