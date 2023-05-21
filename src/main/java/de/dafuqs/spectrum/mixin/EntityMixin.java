package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Inject(method = "onKilledOther", at = @At("HEAD"))
	private void spectrum$rememberKillOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof LivingEntity livingEntity && !livingEntity.getWorld().isClient) {
			LastKillComponent.rememberKillTick(livingEntity, livingEntity.getWorld().getTime());
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
