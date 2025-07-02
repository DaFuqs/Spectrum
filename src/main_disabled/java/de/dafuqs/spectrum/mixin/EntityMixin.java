package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	@Inject(method = "killedEntity", at = @At("HEAD"))
	private void spectrum$rememberKillOther(ServerLevel world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
		Entity entity = (Entity) (Object) this;
		if (entity instanceof LivingEntity livingEntity) {
			LastKillComponent.rememberKillTick(livingEntity, livingEntity.level().getGameTime());
			
			MobEffectInstance frenzy = livingEntity.getEffect(SpectrumStatusEffects.FRENZY);
			if (frenzy != null) {
				((FrenzyStatusEffect) frenzy.getEffect()).onKill(livingEntity, frenzy.getAmplifier());
			}
		}
	}
	
	@ModifyVariable(method = "makeStuckInBlock", at = @At(value = "LOAD"), argsOnly = true)
	private Vec3 spectrum$applyInexorableAntiBlockSlowdown(Vec3 multiplier) {
		var entity = (Entity) (Object) this;
		if (entity instanceof LivingEntity livingEntity && InexorableHelper.isArmorActive(livingEntity)) {
			return Vec3.ZERO;
		}
		return multiplier;
	}
	
	@Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
	private void spectrum$applyInexorableAntiSlowdown(CallbackInfoReturnable<Float> cir) {
		var entity = (Entity) (Object) this;
		if (entity instanceof LivingEntity livingEntity && InexorableHelper.isArmorActive(livingEntity)) {
			cir.setReturnValue(Math.max(cir.getReturnValue(), 1F));
		}
	}
	
	@Inject(method = "spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
	public void spectrum$dropStack(ItemStack stack, CallbackInfoReturnable<ItemEntity> cir) {
		if ((Object) this instanceof LivingEntity thisLivingEntity) {
			if (thisLivingEntity.isDeadOrDying() && thisLivingEntity.getLastHurtByMob() instanceof Player killer) {
				var hasInventoryInsertion = thisLivingEntity.level().registryAccess()
						.lookup(Registries.ENCHANTMENT)
						.flatMap(impl -> impl.get(SpectrumEnchantments.INVENTORY_INSERTION))
						.map(e -> EnchantmentHelper.getEnchantmentLevel(e, killer) > 0)
						.orElse(false);
				if (hasInventoryInsertion) {
					Item item = stack.getItem();
					int count = stack.getCount();
					
					if (killer.getInventory().add(stack)) {
						killer.level().playSound(null, killer.getX(), killer.getY(), killer.getZ(),
								SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
								0.2F, ((killer.getRandom().nextFloat() - killer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
						
						if (stack.isEmpty()) {
							killer.awardStat(Stats.ITEM_PICKED_UP.get(item), count);
							cir.cancel();
						}
						killer.awardStat(Stats.ITEM_PICKED_UP.get(item), count - stack.getCount());
					}
				}
			}
		}
	}
	
	@ModifyReturnValue(method = "getPose", at = @At("RETURN"))
	public Pose spectrum$forceSleepPose(Pose original) {
		var entity = (Entity) (Object) this;
		
		if (!(entity instanceof LivingEntity living))
			return original;
		
		if (!(entity instanceof Player) && (living.hasEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || living.hasEffect(SpectrumStatusEffects.FATAL_SLUMBER)))
			return Pose.SLEEPING;
		
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
