package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.enchantments.CloversFavorEnchantment;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomChanceWithLootingLootCondition.class)
public class RandomChanceWithLootingLootConditionMixin {
	
	@Shadow
	@Final
	float chance;
	
	@Inject(at = @At("RETURN"), method = "test(Lnet/minecraft/loot/context/LootContext;)Z", cancellable = true)
	public void applyRareLootEnchantment(LootContext lootContext, CallbackInfoReturnable<Boolean> cir) {
		// if the result was to not drop a drop before reroll
		// gets more probable with each additional level of Clovers Favor
		if (!cir.getReturnValue() && this.chance < 1.0F) {
			cir.setReturnValue(lootContext.getRandom().nextFloat() < CloversFavorEnchantment.rollChance(this.chance, lootContext.get(LootContextParameters.KILLER_ENTITY)));
		}
	}
	
}
