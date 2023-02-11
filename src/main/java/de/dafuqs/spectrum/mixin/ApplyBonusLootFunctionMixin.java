package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(net.minecraft.loot.function.ApplyBonusLootFunction.class)
public abstract class ApplyBonusLootFunctionMixin {
	
	@Shadow @Final Enchantment enchantment;
	@Shadow @Final ApplyBonusLootFunction.Formula formula;
	
	@ModifyVariable(method = "process(Lnet/minecraft/item/ItemStack;Lnet/minecraft/loot/context/LootContext;)Lnet/minecraft/item/ItemStack;", at = @At("STORE"), ordinal = 1)
	public int spectrum$rerollBonusLoot(int oldValue, ItemStack stack, LootContext context) {
		// if the player has the ANOTHER_DRAW effect the bonus loot of
		// this function gets rerolled potency+1 times and the best one taken
		ItemStack itemStack = context.get(LootContextParameters.TOOL);
		Entity entity = context.get(LootContextParameters.THIS_ENTITY);
		if (itemStack != null && entity instanceof LivingEntity livingEntity) {
			int enchantmentLevel = EnchantmentHelper.getLevel(this.enchantment, itemStack);
			if(enchantmentLevel > 0) {
				StatusEffectInstance effect = livingEntity.getStatusEffect(SpectrumStatusEffects.ANOTHER_ROLL);
				if(effect != null) {
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
