package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Inject(method = "onKilledOther", at = @At("HEAD"))
	private void spectrum$rememberKillOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
		Entity entity = (Entity) (Object) this;
		if (entity instanceof LivingEntity livingEntity) {
			LastKillComponent.rememberKillTick(livingEntity, livingEntity.getWorld().getTime());
			
			StatusEffectInstance frenzy = livingEntity.getStatusEffect(SpectrumStatusEffects.FRENZY);
			if (frenzy != null) {
				((FrenzyStatusEffect) frenzy.getEffectType()).onKill(livingEntity, frenzy.getAmplifier());
			}
		}
	}

	@Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
	private void applyInexorableAntiSlowdown(CallbackInfoReturnable<Float> cir) {
		var entity = (Entity) (Object) this;

		if (entity instanceof LivingEntity livingEntity) {
			var inexorable = EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, livingEntity.getEquippedStack(EquipmentSlot.CHEST));

			if (inexorable > 0)
				cir.setReturnValue(Math.max(cir.getReturnValue(), 1F));
		}
	}

}
