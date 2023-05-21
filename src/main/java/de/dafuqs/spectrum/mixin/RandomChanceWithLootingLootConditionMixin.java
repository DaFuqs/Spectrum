package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.enchantments.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(RandomChanceWithLootingLootCondition.class)
public abstract class RandomChanceWithLootingLootConditionMixin {
	
	@Shadow
	@Final
	float chance;
	
	@Inject(at = @At("RETURN"), method = "test(Lnet/minecraft/loot/context/LootContext;)Z", cancellable = true)
	public void spectrum$applyRareLootEnchantment(LootContext lootContext, CallbackInfoReturnable<Boolean> cir) {
		// if the result was to not drop a drop before reroll
		// gets more probable with each additional level of Clovers Favor
		if (!cir.getReturnValue() && this.chance < 1.0F) {
			cir.setReturnValue(lootContext.getRandom().nextFloat() < CloversFavorEnchantment.rollChance(this.chance, lootContext.get(LootContextParameters.KILLER_ENTITY)));
		}
	}
	
}
