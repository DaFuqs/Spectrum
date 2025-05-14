package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(MobEffectInstance.class)
public interface StatusEffectInstanceAccessor {
	
	@Accessor(value = "duration")
	void setDuration(int newDuration);
	
	@Accessor(value = "amplifier")
	void setAmplifier(int newAmplifier);
	
}