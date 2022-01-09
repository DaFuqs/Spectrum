package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomChanceLootCondition.class)
public class RandomChanceLootConditionMixin {

	@Shadow @Final
	float chance;

	@Inject(at = @At("RETURN"), method= "test(Lnet/minecraft/loot/context/LootContext;)Z", cancellable = true)
	public void applyRareLootEnchantment(LootContext lootContext, CallbackInfoReturnable<Boolean> cir) {
		if(!cir.getReturnValue() && this.chance < 1.0F) {
			Entity entity = lootContext.get(LootContextParameters.KILLER_ENTITY);
			if (entity instanceof LivingEntity && SpectrumEnchantments.CLOVERS_FAVOR.canEntityUse(entity)) {
				int rareLootLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.CLOVERS_FAVOR,((LivingEntity) entity).getMainHandStack());
				if(rareLootLevel > 0) {
					cir.setReturnValue(lootContext.getRandom().nextFloat() < this.chance * (float)rareLootLevel * rareLootLevel);
				}
			}
		}
	}

}
