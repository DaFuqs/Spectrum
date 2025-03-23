package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin extends LivingEntity {

	protected WitherEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setCovetedItem()V"),
			method = "dropEquipment(Lnet/minecraft/entity/damage/DamageSource;IZ)V", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void spawnEntity(DamageSource source, int lootingMultiplier, boolean allowDrops, CallbackInfo ci, ItemEntity itemEntity) {
		Entity attackerEntity = source.getAttacker();
		if (attackerEntity instanceof LivingEntity livingAttacker) {
			World world = attackerEntity.getWorld();
			int cloversFavorLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.CLOVERS_FAVOR, livingAttacker.getMainHandStack(), livingAttacker);
			if (cloversFavorLevel > 0) {
				int additionalCount = (int) (cloversFavorLevel / 2.0F + world.random.nextFloat() * cloversFavorLevel);
				itemEntity.getStack().setCount(itemEntity.getStack().getCount() + additionalCount);
			}
		}
	}

	@ModifyReturnValue(method = "addStatusEffect", at = @At("TAIL"))
	private boolean spectrum$allowWitherNaps(boolean original, @Local(argsOnly = true) StatusEffectInstance effect, @Local(argsOnly = true) Entity source) {
		if (SpectrumStatusEffectTags.isIn(SpectrumStatusEffectTags.SOPORIFIC, effect.getEffectType())) {
			return super.addStatusEffect(effect, source);
		}

		return original;
	}
}
