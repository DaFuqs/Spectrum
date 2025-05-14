package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Projectile.class)
public abstract class ProjectileEntityMixin {
	
	@Shadow
	public abstract void shoot(double x, double y, double z, float speed, float divergence);
	
	@Inject(at = @At("HEAD"), method = "onHitEntity", cancellable = true)
	protected void onProjectileHit(EntityHitResult entityHitResult, CallbackInfo ci) {
		// if the target has a Puff circlet equipped
		// protect it from this projectile
		Projectile thisEntity = (Projectile) (Object) this;
		if (!thisEntity.getType().is(SpectrumEntityTypeTags.UNDEFLECTABLE)) {
			Level world = thisEntity.level();
			if (!world.isClientSide) {
				Entity entity = entityHitResult.getEntity();
				if (entity instanceof LivingEntity livingEntity) {
					boolean protect = false;
					
					MobEffectInstance reboundInstance = livingEntity.getEffect(SpectrumStatusEffects.PROJECTILE_REBOUND);
					if (reboundInstance != null && entity.level().getRandom().nextFloat() < SpectrumStatusEffects.PROJECTILE_REBOUND_CHANCE_PER_LEVEL * reboundInstance.getAmplifier()) {
						protect = true;
					} else {
						Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
						if (component.isPresent()) {
							List<Tuple<SlotReference, ItemStack>> equipped = component.get().getEquipped(SpectrumItems.PUFF_CIRCLET);
							if (!equipped.isEmpty()) {
								var charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
								if (charges > 0) {
									AzureDikeProvider.absorbDamage(livingEntity, PuffCircletItem.PROJECTILE_DEFLECTION_COST);
									protect = true;
								}
							}
						}
					}
					
					if (protect) {
						this.shoot(0, 0, 0, 0, 0);
						
						PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, thisEntity.position(),
								ColoredCraftingParticleEffect.WHITE, 6,
								new Vec3(0, 0, 0),
								new Vec3(thisEntity.getX() - livingEntity.position().x, thisEntity.getY() - livingEntity.position().y, thisEntity.getZ() - livingEntity.position().z));
						PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, thisEntity.position(),
								ColoredCraftingParticleEffect.BLUE, 6,
								new Vec3(0, 0, 0),
								new Vec3(thisEntity.getX() - livingEntity.position().x, thisEntity.getY() - livingEntity.position().y, thisEntity.getZ() - livingEntity.position().z));
						
						world.playSound(null, thisEntity.blockPosition(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundSource.PLAYERS, 1.0F, 1.0F);
						livingEntity.hurtTime = Math.max(livingEntity.hurtTime, 1);
						ci.cancel();
					}
					
				}
			}
		}
	}
	
}
