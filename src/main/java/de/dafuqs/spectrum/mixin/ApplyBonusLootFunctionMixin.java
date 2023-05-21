package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.loot.function.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ApplyBonusLootFunction.class)
public abstract class ApplyBonusLootFunctionMixin {
	
	@Shadow
	@Final
	Enchantment enchantment;
	@Shadow
	@Final
	ApplyBonusLootFunction.Formula formula;
	
	@ModifyVariable(method = "process(Lnet/minecraft/item/ItemStack;Lnet/minecraft/loot/context/LootContext;)Lnet/minecraft/item/ItemStack;", at = @At("STORE"), ordinal = 1)
	public int spectrum$rerollBonusLoot(int oldValue, ItemStack stack, LootContext context) {
		// if the player has the ANOTHER_DRAW effect the bonus loot of
		// this function gets rerolled potency+1 times and the best one taken
		ItemStack itemStack = context.get(LootContextParameters.TOOL);
		Entity entity = context.get(LootContextParameters.THIS_ENTITY);
		if (itemStack != null && entity instanceof LivingEntity livingEntity) {
			int enchantmentLevel = EnchantmentHelper.getLevel(this.enchantment, itemStack);
			if (enchantmentLevel > 0) {
				StatusEffectInstance effect = livingEntity.getStatusEffect(SpectrumStatusEffects.ANOTHER_ROLL);
				if (effect != null) {
					int rollCount = effect.getAmplifier() + 1;
					int highestRoll = oldValue;
					for (int i = 0; i < rollCount; i++) {
						int thisRoll = this.formula.getValue(context.getRandom(), stack.getCount(), enchantmentLevel);
						highestRoll = Math.max(highestRoll, thisRoll);
					}
					return highestRoll;
				}
			}
		}
		return oldValue;
	}
	
}
