package de.dafuqs.spectrum.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {

	@ModifyVariable(method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z", at = @At(value = "STORE"), ordinal = 0, index = 1, name = "predicate")
	public Predicate<Entity> addItemEntityPredicate(Predicate<Entity> predicate) {
		return predicate.or(entity -> entity instanceof ItemEntity);
	}

}
