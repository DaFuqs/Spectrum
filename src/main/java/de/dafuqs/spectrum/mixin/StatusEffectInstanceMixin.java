package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.injectors.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;

@Mixin(MobEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceInjector {
	
	@Shadow
	private int duration;
	@Shadow
	private int amplifier;
	
	@Override
	public void spectrum$setDuration(int newDuration) {
		this.duration = newDuration;
	}
	
	@Override
	public void spectrum$setAmplifier(int newAmplifier) {
		this.amplifier = newAmplifier;
	}
	
}
