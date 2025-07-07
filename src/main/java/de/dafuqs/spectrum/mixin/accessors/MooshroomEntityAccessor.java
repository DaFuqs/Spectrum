package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.entity.animal.*;
import net.minecraft.world.item.component.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(MushroomCow.class)
public interface MooshroomEntityAccessor {
	
	@Accessor
	SuspiciousStewEffects getStewEffects();
	
	@Accessor
	void setStewEffects(SuspiciousStewEffects stewEffects);
	
}
