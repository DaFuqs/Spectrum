package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
	
	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@WrapOperation(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private boolean makeBidentDamageReasonable(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
		if (((Object) this) instanceof BidentBaseEntity bidentEntity) {
			var stack = bidentEntity.getTrackedStack();
			var damage = getDamage(stack) + 1;
			
			if (instance instanceof LivingEntity livingAttacked) {
				damage += EnchantmentHelper.getAttackDamage(stack, livingAttacked.getGroup());
			}
			
			return instance.damage(SpectrumDamageTypes.impaling(getWorld(), bidentEntity, getOwner()), damage * 2);
		} else {
			return original.call(instance, source, amount);
		}
	}
	
	@Unique
	private float getDamage(ItemStack stack) {
		return (float) stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
				.stream()
				.mapToDouble(EntityAttributeModifier::getValue)
				.sum();
	}
}
