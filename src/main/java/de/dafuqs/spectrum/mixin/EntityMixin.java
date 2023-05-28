package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.stat.*;
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
	private void spectrum$applyInexorableAntiSlowdown(CallbackInfoReturnable<Float> cir) {
		var entity = (Entity) (Object) this;

		if (entity instanceof LivingEntity livingEntity) {
			var inexorable = EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, livingEntity.getEquippedStack(EquipmentSlot.CHEST));

			if (inexorable > 0)
				cir.setReturnValue(Math.max(cir.getReturnValue(), 1F));
		}
	}

	@Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
	public void spectrum$dropStack(ItemStack stack, CallbackInfoReturnable<ItemEntity> cir) {
		if ((Object) this instanceof LivingEntity thisLivingEntity) {
			if (thisLivingEntity.isDead() && thisLivingEntity.getAttacker() instanceof PlayerEntity killer) {
				if (EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.INVENTORY_INSERTION, killer) > 0) {
					Item item = stack.getItem();
					int count = stack.getCount();

					if (killer.getInventory().insertStack(stack)) {
						if (stack.isEmpty()) {
							killer.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), count);
							cir.cancel();
						}
						killer.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), count - stack.getCount());
					}
				}
			}
		}
	}

}
