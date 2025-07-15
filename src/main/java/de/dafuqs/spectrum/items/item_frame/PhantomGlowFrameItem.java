package de.dafuqs.spectrum.items.item_frame;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.level.*;

public class PhantomGlowFrameItem extends SpectrumItemFrameItem {
	
	public PhantomGlowFrameItem(EntityType<? extends HangingEntity> entityType, Properties settings) {
		super(entityType, settings);
	}
	
	@Override
	public ItemFrame getItemFrameEntity(Level world, BlockPos blockPos, Direction direction) {
		return new PhantomGlowFrameEntity(SpectrumEntityTypes.GLOW_PHANTOM_FRAME, world, blockPos, direction);
	}
	
}
