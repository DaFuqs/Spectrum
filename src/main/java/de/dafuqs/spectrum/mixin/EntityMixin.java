package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.util.math.*;
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

	@ModifyVariable(method = "slowMovement", at = @At(value = "LOAD"), argsOnly = true)
	private Vec3d spectrum$applyInexorableAntiBlockSlowdown(Vec3d multiplier) {
		if ((Object) this instanceof LivingEntity livingEntity && InexorableEnchantment.isArmorActive(livingEntity)) {
			return Vec3d.ZERO;
		}
		return multiplier;
	}
	
	@Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
	private void spectrum$applyInexorableAntiSlowdown(CallbackInfoReturnable<Float> cir) {
		if ((Object) this instanceof LivingEntity livingEntity && InexorableEnchantment.isArmorActive(livingEntity)) {
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
						killer.getWorld().playSound(null, killer.getX(), killer.getY(), killer.getZ(),
								SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
								0.2F, ((killer.getRandom().nextFloat() - killer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
						
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

	@ModifyReturnValue(method = "getPose", at = @At("RETURN"))
	public EntityPose spectrum$forceSleepPose(EntityPose original) {
		var entity = (Entity) (Object) this;
		
		if (!(entity instanceof LivingEntity living))
			return original;

		if (!(entity instanceof PlayerEntity) && (living.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || living.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)))
			return EntityPose.SLEEPING;

		return original;
	}
	
	@ModifyReturnValue(method = "isOnFire", at = @At("RETURN"))
	public boolean spectrum$considerPrimfireAsFire(boolean original) {
		var entity = (Entity) (Object) this;
		
		if (entity instanceof LivingEntity living && OnPrimordialFireComponent.isOnPrimordialFire(living))
			return true;

		return original;
	}
}
