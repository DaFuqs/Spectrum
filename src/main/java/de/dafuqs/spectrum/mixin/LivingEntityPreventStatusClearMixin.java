package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.v2.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityPreventStatusClearMixin {
	
	@Shadow
	public abstract boolean addEffect(MobEffectInstance effect);
	
	@Shadow
	public abstract Map<MobEffect, MobEffectInstance> getActiveEffectsMap();
	
	@Inject(method = "removeAllEffects", at = @At("HEAD"))
	private void spectrum$detectFatalSlumber(CallbackInfoReturnable<Boolean> cir, @Share("hasFatalSlumber") LocalBooleanRef hasFatalSlumber) {
		hasFatalSlumber.set(getActiveEffectsMap().containsKey(SpectrumMobEffects.FATAL_SLUMBER.value()));
	}
	
	@Inject(method = "removeAllEffects", at = @At("TAIL"))
	private void spectrum$applyEternalSlumberIfFatalSlumberRemoved(CallbackInfoReturnable<Boolean> cir, @Share("hasFatalSlumber") LocalBooleanRef hasFatalSlumber) {
		if (hasFatalSlumber.get()) {
			addEffect(new MobEffectInstance(SpectrumMobEffects.ETERNAL_SLUMBER, 6000));
		}
	}
	
	@WrapWithCondition(method = "removeAllEffects", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V"))
	private boolean spectrum$preventStatusClear2(Iterator instance, @Share("blockRemoval") LocalBooleanRef blockRemoval) {
		if (blockRemoval.get()) {
			blockRemoval.set(false);
			return false;
		}
		return true;
	}
	
}