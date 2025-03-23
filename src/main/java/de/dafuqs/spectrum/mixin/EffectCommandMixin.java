package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.api.status_effect.Incurable;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.EffectCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(EffectCommand.class)
public class EffectCommandMixin {

    @Inject(method = "executeClear(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    private static void clearIncurableEffects(ServerCommandSource source, Collection<? extends Entity> targets, CallbackInfoReturnable<Integer> cir, @Local Entity target) {
        if (target instanceof LivingEntity living) {
            for (StatusEffectInstance effect : living.getStatusEffects()) {
                if (((Incurable) effect).spectrum$isIncurable())
                    ((Incurable) effect).spectrum$setIncurable(false);
            }
            // manually remove fatal slumber to bypass turning it into eternal slumber
            living.removeStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER);
        }
    }

    @Inject(method = "executeClear(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Lnet/minecraft/registry/entry/RegistryEntry;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;removeStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    private static void clearIncurableEffects(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry<StatusEffect> statusEffect, CallbackInfoReturnable<Integer> cir, @Local Entity target, @Local StatusEffect ref) {
        if (target instanceof LivingEntity living) {
            var effect = living.getStatusEffect(ref);
            if (effect != null) {
                if (((Incurable) effect).spectrum$isIncurable())
                    ((Incurable) effect).spectrum$setIncurable(false);
            }
        }
    }
}
