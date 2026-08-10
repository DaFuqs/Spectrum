package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LootItemRandomChanceCondition.class)
public abstract class RandomChanceLootConditionMixin {
	
	@ModifyVariable(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At("STORE"))
	public float spectrum$applyRareLootEnchantment(float originalChance, LootContext context) {
		Entity entity = context.hasParam(LootContextParams.ATTACKING_ENTITY)
			? context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) // when attacking
			: context.getParamOrNull(LootContextParams.THIS_ENTITY); // when breaking blooks, fishing, ...
		return SpectrumEntityAttributes.modifyLootChance(originalChance, entity);
	}
	
}
