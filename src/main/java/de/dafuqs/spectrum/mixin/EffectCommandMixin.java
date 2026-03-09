package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.commands.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.commands.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

// TODO: needed?
@Mixin(EffectCommands.class)
public class EffectCommandMixin {
	
	@Inject(method = "clearEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"))
	private static void clearUncurableEffects(CommandSourceStack source, Collection<? extends Entity> targets, CallbackInfoReturnable<Integer> cir, @Local Entity target) {
		if (target instanceof LivingEntity living) {
			for (MobEffectInstance effect : living.getActiveEffects()) {
				effect.getCures().remove(SpectrumEffectCures.COMMAND_ONLY);
			}
			// manually remove fatal slumber to bypass turning it into eternal slumber
			living.removeEffect(SpectrumMobEffects.FATAL_SLUMBER);
		}
	}
	
	@Inject(method = "clearEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeEffect(Lnet/minecraft/core/Holder;)Z"))
	private static void clearUncurableEffect(CommandSourceStack source, Collection<? extends Entity> targets, Holder<MobEffect> statusEffect, CallbackInfoReturnable<Integer> cir, @Local Entity target, @Local MobEffect ref) {
		if (target instanceof LivingEntity living) {
			var effect = living.getEffect(living.level().registryAccess().registryOrThrow(Registries.MOB_EFFECT).wrapAsHolder(ref));
			if (effect != null) {
				effect.getCures().remove(SpectrumEffectCures.COMMAND_ONLY);
			}
		}
	}
}
