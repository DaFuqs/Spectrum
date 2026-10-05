package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LootItemRandomChanceWithEnchantedBonusCondition.class)
public abstract class RandomChanceWithEnchantedBonusLootConditionMixin {
	
	@ModifyVariable(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At("STORE"))
	public float spectrum$applyRareLootEnchantment(float original, LootContext context) {
		if(original <= 0) {
			return original;
		}
		return SpectrumEntityAttributes.modifyLootChance(original, context.getParamOrNull(LootContextParams.ATTACKING_ENTITY));
	}
	
}
