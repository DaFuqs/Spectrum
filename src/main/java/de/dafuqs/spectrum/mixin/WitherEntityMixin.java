package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.damage.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin {
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setCovetedItem()V"),
			method = "dropEquipment(Lnet/minecraft/entity/damage/DamageSource;IZ)V", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void spawnEntity(DamageSource source, int lootingMultiplier, boolean allowDrops, CallbackInfo ci, ItemEntity itemEntity) {
		Entity attackerEntity = source.getAttacker();
		if (attackerEntity instanceof LivingEntity livingAttacker) {
			int cloversFavorLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.CLOVERS_FAVOR, livingAttacker.getMainHandStack(), livingAttacker);
			if (cloversFavorLevel > 0) {
				int additionalCount = (int) (cloversFavorLevel / 2.0F + ((WitherEntity) (Object) this).getWorld().random.nextFloat() * cloversFavorLevel);
				itemEntity.getStack().setCount(itemEntity.getStack().getCount() + additionalCount);
			}
		}
	}
	
}
