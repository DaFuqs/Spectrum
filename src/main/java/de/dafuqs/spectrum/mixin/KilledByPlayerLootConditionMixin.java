package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(LootItemKilledByPlayerCondition.class)
public abstract class KilledByPlayerLootConditionMixin {
	
	@Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At(value = "RETURN"), cancellable = true)
	private void spectrum$testDropPlayerLoot(LootContext lootContext, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			DamageSource damageSource = lootContext.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
			if (damageSource != null && damageSource.is(SpectrumDamageTypeTags.DROPS_LOOT_LIKE_PLAYERS)) {
				cir.setReturnValue(true);
			}
		}
	}
	
}
