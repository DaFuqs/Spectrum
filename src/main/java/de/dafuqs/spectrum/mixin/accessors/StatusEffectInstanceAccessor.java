package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(StatusEffectInstance.class)
public interface StatusEffectInstanceAccessor {
	
	@Accessor(value = "duration")
	void setDuration(int newDuration);
	
}