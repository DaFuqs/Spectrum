package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.dafuqs.spectrum.entity.entity.BidentBaseEntity;
import de.dafuqs.spectrum.registries.SpectrumDamageTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

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
        }
        else {
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
