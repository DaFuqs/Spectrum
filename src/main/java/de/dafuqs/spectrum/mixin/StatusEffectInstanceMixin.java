package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.injectors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(MobEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceInjector {
	
	@Shadow
	private int duration;
	@Shadow
	private int amplifier;
	
	@Shadow
	@Final
	private Holder<MobEffect> effect;
	
	@Override
	public void spectrum$setDuration(int newDuration) {
		this.duration = newDuration;
	}
	
	@Override
	public void spectrum$setAmplifier(int newAmplifier) {
		this.amplifier = newAmplifier;
	}
	
	@Inject(at = @At("HEAD"), method = "isInfiniteDuration", cancellable = true)
	public void isInfiniteDuration(CallbackInfoReturnable<Boolean> cir) {
		if(this.effect.is(SpectrumMobEffectTags.ALWAYS_INFINITE)) {
			cir.setReturnValue(true);
		}
	}
	
}
