package de.dafuqs.spectrum.items.item_frame;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class PhantomFrameItem extends SpectrumItemFrameItem {
	
	public PhantomFrameItem(EntityType<? extends AbstractDecorationEntity> entityType, Settings settings) {
		super(entityType, settings);
	}
	
	@Override
	public ItemFrameEntity getItemFrameEntity(World world, BlockPos blockPos, Direction direction) {
		return new PhantomFrameEntity(SpectrumEntityTypes.PHANTOM_FRAME, world, blockPos, direction);
	}
	
}
