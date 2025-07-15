package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.*;
import net.minecraft.world.entity.ai.memory.*;
import net.minecraft.world.entity.schedule.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Brain.class)
public abstract class BrainMixin<E extends LivingEntity> {
	
	@Shadow
	public abstract void setActiveActivityIfPossible(Activity activity);
	
	@Shadow
	public abstract <U> void eraseMemory(MemoryModuleType<U> type);
	
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void slowDownBrainTicks(ServerLevel world, E entity, CallbackInfo ci) {
		if (entity.hasEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || entity.hasEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
			ci.cancel();
			return;
		}
		
		var effect = entity.getEffect(SpectrumStatusEffects.SOMNOLENCE);
		if (effect == null)
			return;
		
		var scaling = SleepStatusEffect.getSleepScaling(entity);
		if (scaling <= 0 || entity.getRandom().nextFloat() > Math.min(scaling * 0.05, 0.3))
			return;
		
		if (entity.getRandom().nextFloat() < scaling * 0.5) {
			eraseMemory(MemoryModuleType.ANGRY_AT);
			setActiveActivityIfPossible(Activity.REST);
		}
		
		ci.cancel();
	}
}
