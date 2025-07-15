package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LootItemRandomChanceCondition.class)
public abstract class RandomChanceLootConditionMixin {
	
	@Shadow
	@Final
	private NumberProvider chance;
	
	@ModifyReturnValue(at = @At("RETURN"), method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z")
	public boolean spectrum$applyRareLootEnchantment(boolean original, LootContext context) {
		// if the result was to not drop a drop before reroll
		// gets more probable with each additional level of Clovers Favor
		if (!original) {
			original = context.getRandom().nextFloat() < CloversFavorHelper.rollChance(chance.getFloat(context), context.getParamOrNull(LootContextParams.ATTACKING_ENTITY));
		}
		return original;
	}
	
}
