package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {
	
	@Shadow
	public abstract void setVelocity(double x, double y, double z, float speed, float divergence);
	
	@Inject(at = @At("HEAD"), method = "onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V", cancellable = true)
	protected void onProjectileHit(EntityHitResult entityHitResult, CallbackInfo ci) {
		// if the target has a Puff circlet equipped
		// protect it from this projectile
		ProjectileEntity thisEntity = (ProjectileEntity) (Object) this;
		if (!thisEntity.getWorld().isClient) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof LivingEntity livingEntity) {
				boolean protect = false;
				
				StatusEffectInstance reboundInstance = livingEntity.getStatusEffect(SpectrumStatusEffects.PROJECTILE_REBOUND);
				if (reboundInstance != null && entity.world.getRandom().nextFloat() < SpectrumStatusEffects.PROJECTILE_REBOUND_CHANCE_PER_LEVEL * reboundInstance.getAmplifier()) {
					protect = true;
				} else {
					Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
					if (component.isPresent()) {
						List<Pair<SlotReference, ItemStack>> equipped = component.get().getEquipped(SpectrumItems.PUFF_CIRCLET);
						if (!equipped.isEmpty()) {
							int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
							if (charges > 0) {
								AzureDikeProvider.absorbDamage(livingEntity, PuffCircletItem.PROJECTILE_DEFLECTION_COST);
								protect = true;
							}
						}
					}
				}
				
				if (protect) {
					this.setVelocity(0, 0, 0, 0, 0);
					
					SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) thisEntity.getWorld(), thisEntity.getPos(),
							SpectrumParticleTypes.WHITE_CRAFTING, 6,
							new Vec3d(0, 0, 0),
							new Vec3d(thisEntity.getX() - livingEntity.getPos().x, thisEntity.getY() - livingEntity.getPos().y, thisEntity.getZ() - livingEntity.getPos().z));
					SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) thisEntity.getWorld(), thisEntity.getPos(),
							SpectrumParticleTypes.BLUE_CRAFTING, 6,
							new Vec3d(0, 0, 0),
							new Vec3d(thisEntity.getX() - livingEntity.getPos().x, thisEntity.getY() - livingEntity.getPos().y, thisEntity.getZ() - livingEntity.getPos().z));
					
					thisEntity.getWorld().playSound(null, thisEntity.getBlockPos(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundCategory.PLAYERS, 1.0F, 1.0F);
					ci.cancel();
				}
				
			}
		}
	}
	
	
}
