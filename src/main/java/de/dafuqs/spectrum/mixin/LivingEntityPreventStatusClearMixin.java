package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.v2.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.mob_effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityPreventStatusClearMixin {
	
	@Shadow
	public abstract void remove(Entity.RemovalReason reason);
	
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
	
	@WrapWithCondition(method = "removeAllEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onEffectRemoved(Lnet/minecraft/world/effect/MobEffectInstance;)V"))
	private boolean spectrum$preventStatusClear(LivingEntity instance, MobEffectInstance effect, @Share("blockRemoval") LocalBooleanRef blockRemoval) {
		if (StatusEffectHelper.isSevere(effect)) {
			if (affectedByImmunity(instance, effect.getAmplifier()))
				return true;
			
			MobEffectHelper.cutDuration(instance, effect);
			
			blockRemoval.set(true);
			return false;
		}
		return true;
	}
	
	@WrapWithCondition(method = "removeAllEffects", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V"))
	private boolean spectrum$preventStatusClear2(Iterator instance, @Share("blockRemoval") LocalBooleanRef blockRemoval) {
		if (blockRemoval.get()) {
			blockRemoval.set(false);
			return false;
		}
		return true;
	}
	
	@WrapOperation(method = "removeEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeEffectNoUpdate(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/effect/MobEffectInstance;"))
	private MobEffectInstance spectrum$preventStatusRemoval(LivingEntity instance, Holder<MobEffect> effectRegistryEntry, Operation<MobEffectInstance> original) {
		var effect = instance.getEffect(effectRegistryEntry);
		boolean cancel;
		
		if (effect == null)
			return original.call(instance, effectRegistryEntry);
		
		cancel = StatusEffectHelper.isSevere(effect);
		
		if (cancel) {
			cancel = !affectedByImmunity(instance, effect.getAmplifier());
		}
		
		if (cancel)
			return null;
		
		return original.call(instance, effectRegistryEntry);
	}
	
	@Unique
	private static boolean affectedByImmunity(LivingEntity instance, int amplifier) {
		var immunity = instance.getEffect(SpectrumMobEffects.IMMUNITY);
		var cost = 1200 + 600 * amplifier;
		
		if (immunity != null && immunity.getDuration() >= cost) {
			immunity.spectrum$setDuration(Math.max(5, immunity.getDuration() - cost));
			if (!instance.level().isClientSide()) {
				((ServerLevel) instance.level()).getChunkSource().broadcastAndSend(instance, new ClientboundUpdateMobEffectPacket(instance.getId(), immunity, false));
			}
			return true;
		}
		return false;
	}
	
}