package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin {
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setCovetedItem()V"),
			method = "dropEquipment(Lnet/minecraft/entity/damage/DamageSource;IZ)V", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void spawnEntity(DamageSource source, int lootingMultiplier, boolean allowDrops, CallbackInfo ci, ItemEntity itemEntity) {
		Entity attackerEntity = source.getAttacker();
		if (attackerEntity instanceof LivingEntity livingAttacker) {
			int cloversFavorLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.CLOVERS_FAVOR, livingAttacker.getMainHandStack());
			if (cloversFavorLevel > 0) {
				int additionalCount = (int) (cloversFavorLevel / 2.0F + ((WitherEntity) (Object) this).getWorld().random.nextFloat() * cloversFavorLevel);
				itemEntity.getStack().setCount(itemEntity.getStack().getCount() + additionalCount);
				
				
			}
		}
	}
	
}
