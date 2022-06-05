package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import de.dafuqs.spectrum.items.trinkets.PuffCircletItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

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
				Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
				if (component.isPresent()) {
					List<Pair<SlotReference, ItemStack>> equipped = component.get().getEquipped(SpectrumItems.PUFF_CIRCLET);
					if (!equipped.isEmpty()) {
						int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
						if (charges > 0) {
							AzureDikeProvider.absorbDamage(livingEntity, PuffCircletItem.PROJECTILE_DEFLECTION_COST);
							
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
	}
	
}
