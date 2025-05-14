package de.dafuqs.spectrum.items.item_frame;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.level.*;

public class PhantomFrameItem extends SpectrumItemFrameItem {
	
	public PhantomFrameItem(EntityType<? extends HangingEntity> entityType, Properties settings) {
		super(entityType, settings);
	}
	
	@Override
	public ItemFrame getItemFrameEntity(Level world, BlockPos blockPos, Direction direction) {
		return new PhantomFrameEntity(SpectrumEntityTypes.PHANTOM_FRAME, world, blockPos, direction);
	}
	
}
