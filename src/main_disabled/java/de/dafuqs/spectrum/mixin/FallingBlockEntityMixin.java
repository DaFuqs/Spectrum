package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {
	
	/**
	 * By default, falling blocks only damage living entities
	 * This mixin runs a second check if we are dealing anvil damage and if yes, triggers anvil crushing
	 */
	@Inject(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
	private void spectrum$processAnvilCrushing(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) DamageSource damageSource2, @Local(ordinal = 2) float fallHurt) {
		if (damageSource2.is(DamageTypes.FALLING_ANVIL)) {
			FallingBlockEntity thisEntity = (FallingBlockEntity) (Object) this;
			thisEntity.level().getEntities(EntityTypeTest.forClass(ItemEntity.class), thisEntity.getBoundingBox(), Entity::isAlive)
					.forEach((entity) -> AnvilCrusher.crush(entity, fallHurt));
		}
	}
	
}
