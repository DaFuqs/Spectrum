package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.function.*;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {
	
	/**
	 * By default, falling blocks only damage living entities
	 * This mixin runs a second check if we are dealing anvil damage and if yes, triggers anvil crushing
	 */
	@Inject(method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void spectrum$processAnvilCrushing(float fallDistance, float arg1, DamageSource arg2, CallbackInfoReturnable<Boolean> cir, int i, Predicate<Entity> predicate, DamageSource damageSource2, float f, Block var8) {
		if (damageSource2.isOf(DamageTypes.FALLING_ANVIL)) {
			FallingBlockEntity thisEntity = (FallingBlockEntity) (Object) this;
			thisEntity.getWorld().getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), thisEntity.getBoundingBox(), Entity::isAlive).forEach((entity) -> {
				AnvilCrusher.crush(entity, f);
			});
		}
	}
	
}
