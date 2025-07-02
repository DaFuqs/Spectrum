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

@Mixin(EffectCommands.class)
public class EffectCommandMixin {
	
	@Inject(method = "clearEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"))
	private static void clearSevereEffects(CommandSourceStack source, Collection<? extends Entity> targets, CallbackInfoReturnable<Integer> cir, @Local Entity target) {
        if (target instanceof LivingEntity living) {
			for (MobEffectInstance effect : living.getActiveEffects()) {
				if (effect.spectrum$isSevere())
					effect.spectrum$setSevere(false);
            }
			// manually remove fatal slumber to bypass turning it into eternal slumber
			living.removeEffect(SpectrumStatusEffects.FATAL_SLUMBER);
        }
    }
	
	@Inject(method = "clearEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeEffect(Lnet/minecraft/core/Holder;)Z"))
	private static void clearSevereEffects(CommandSourceStack source, Collection<? extends Entity> targets, Holder<MobEffect> statusEffect, CallbackInfoReturnable<Integer> cir, @Local Entity target, @Local MobEffect ref) {
        if (target instanceof LivingEntity living) {
			var effect = living.getEffect(living.level().registryAccess().registryOrThrow(Registries.MOB_EFFECT).wrapAsHolder(ref));
            if (effect != null) {
				if (effect.spectrum$isSevere())
					effect.spectrum$setSevere(false);
            }
        }
    }
}
