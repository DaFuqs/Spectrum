package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.entity.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {
	
	@Accessor("pickupDelay")
	int getPickupDelay();
	
}