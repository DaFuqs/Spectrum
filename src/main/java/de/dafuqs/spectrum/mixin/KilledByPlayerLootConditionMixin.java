package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KilledByPlayerLootCondition.class)
public class KilledByPlayerLootConditionMixin {
	
	@Inject(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At(value = "RETURN"), cancellable = true)
	private void spectrum$testDropPlayerLoot(LootContext lootContext, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			DamageSource damageSource = lootContext.get(LootContextParameters.DAMAGE_SOURCE);
			if (damageSource instanceof SpectrumDamageSources.SpectrumDamageSource spectrumDamageSource && spectrumDamageSource.dropsPlayerLoot()) {
				cir.setReturnValue(true);
			}
		}
	}
	
}
