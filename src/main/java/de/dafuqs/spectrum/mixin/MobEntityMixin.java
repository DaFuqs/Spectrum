package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.mob_effect.*;
import net.minecraft.world.entity.*;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Mob.class)
public abstract class MobEntityMixin {
	
	@Shadow
	private @Nullable LivingEntity target;
	
	@Inject(method = "serverAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;level()Lnet/minecraft/world/level/Level;", ordinal = 0), cancellable = true)
	public void slowDownAIticks(CallbackInfo ci) {
		var entity = (Mob) (Object) this;
		
		if ((entity.hasEffect(SpectrumMobEffects.ETERNAL_SLUMBER) || entity.hasEffect(SpectrumMobEffects.FATAL_SLUMBER)) && !SleepMobEffect.isImmuneish(entity)) {
			target = null;
			ci.cancel();
			return;
		}
		
		var potency = SleepMobEffect.getSleepScaling(entity);
		
		if (potency <= 0 || entity.getRandom().nextFloat() > potency * 0.15)
			return;
		
		if (entity.getRandom().nextFloat() < potency * 0.75)
			target = null;
		
		ci.cancel();
	}
}
