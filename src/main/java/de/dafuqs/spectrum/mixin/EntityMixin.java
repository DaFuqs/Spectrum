package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.LastKillComponent;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
