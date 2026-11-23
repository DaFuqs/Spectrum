package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ThrownTrident.class)
public abstract class TridentEntityMixin extends AbstractArrow {
	
	protected TridentEntityMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
		super(entityType, world);
	}
	
	@WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private boolean makeBidentDamageReasonable(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
		if (((Object) this) instanceof BidentBaseEntity bidentEntity) {
			var stack = bidentEntity.getPickupItemStackOrigin();
			float damage = AttributeHelper.computeValue(getOwner(), stack, Attributes.ATTACK_DAMAGE, EquipmentSlot.MAINHAND);
			
			DamageSource damageSource = SpectrumDamageTypes.impaling(level(), bidentEntity, getOwner());
			if (this.level() instanceof ServerLevel serverWorld) {
				damage = EnchantmentHelper.modifyDamage(serverWorld, this.getWeaponItem(), instance, damageSource, damage);
			}
			
			return instance.hurt(damageSource, damage * 2);
		}
		return original.call(instance, source, amount);
	}
	
}
