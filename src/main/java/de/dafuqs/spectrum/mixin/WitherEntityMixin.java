package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.wither.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(WitherBoss.class)
public abstract class WitherEntityMixin extends LivingEntity {
	
	protected WitherEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setExtendedLifetime()V"),
			method = "dropCustomDeathLoot", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void spawnEntity(ServerLevel world, DamageSource source, boolean causedByPlayer, CallbackInfo ci, ItemEntity itemEntity) {
		Entity attackerEntity = source.getEntity();
		if (attackerEntity instanceof LivingEntity livingAttacker) {
			int cloversFavorLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), SpectrumEnchantments.CLOVERS_FAVOR, livingAttacker.getMainHandItem());
			if (cloversFavorLevel > 0) {
				int additionalCount = (int) (cloversFavorLevel / 2.0F + world.getRandom().nextFloat() * cloversFavorLevel);
				itemEntity.getItem().setCount(itemEntity.getItem().getCount() + additionalCount);
			}
		}
	}
	
	@ModifyReturnValue(method = "addEffect", at = @At("TAIL"))
	private boolean spectrum$allowWitherNaps(boolean original, @Local(argsOnly = true) MobEffectInstance effect, @Local(argsOnly = true) Entity source) {
		if (effect.getEffect().is(SpectrumStatusEffectTags.SOPORIFIC)) {
			return super.addEffect(effect, source);
		}
		return original;
	}
}
