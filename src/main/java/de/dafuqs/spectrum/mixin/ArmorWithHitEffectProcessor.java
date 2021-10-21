package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.interfaces.ArmorWithHitEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ArmorWithHitEffectProcessor {

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z", ordinal = 1))
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!((LivingEntity)(Object) this).world.isClient) {
			if (((Object) this) instanceof MobEntity) {
				MobEntity thisMobEntity = (MobEntity) (Object) this;
				for (ItemStack armorItemStack : thisMobEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect) {
						((ArmorWithHitEffect) armorItemStack.getItem()).onHit(armorItemStack, source, thisMobEntity, amount);
					}
				}
			} else if (((Object) this) instanceof ServerPlayerEntity) {
				ServerPlayerEntity thisPlayerEntity = (ServerPlayerEntity) (Object) this;
				for (ItemStack armorItemStack : thisPlayerEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect) {
						((ArmorWithHitEffect) armorItemStack.getItem()).onHit(armorItemStack, source, thisPlayerEntity, amount);
					}
				}
			}
		}
	}

}
