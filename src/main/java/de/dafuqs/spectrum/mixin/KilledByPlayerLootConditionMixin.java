package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.damage.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(KilledByPlayerLootCondition.class)
public abstract class KilledByPlayerLootConditionMixin {
	
	@Inject(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At(value = "RETURN"), cancellable = true)
	private void spectrum$testDropPlayerLoot(LootContext lootContext, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			DamageSource damageSource = lootContext.get(LootContextParameters.DAMAGE_SOURCE);
			if (damageSource.isIn(SpectrumDamageSources.FAKE_PLAYER_DAMAGE)) {
				cir.setReturnValue(true);
			}
		}
	}
	
}
