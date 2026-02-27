package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ApplyBonusCount.class)
public abstract class ApplyBonusLootFunctionMixin {
	
	@Shadow
	@Final
	private Holder<Enchantment> enchantment;
	
	@Shadow
	@Final
	private ApplyBonusCount.Formula formula;
	
	@ModifyVariable(method = "run", at = @At("STORE"), ordinal = 1)
	public int spectrum$rerollBonusLoot(int oldValue, ItemStack stack, LootContext context) {
		// if the player has the ANOTHER_DRAW effect the bonus loot of
		// this function gets rerolled potency+1 times and the best one taken
		ItemStack itemStack = context.getParamOrNull(LootContextParams.TOOL);
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if (itemStack != null && entity instanceof LivingEntity livingEntity) {
			int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, itemStack);
			if (enchantmentLevel > 0) {
				MobEffectInstance effect = livingEntity.getEffect(SpectrumStatusEffects.ANOTHER_ROLL);
				if (effect != null) {
					int rollCount = effect.getAmplifier() + 1;
					int highestRoll = oldValue;
					for (int i = 0; i < rollCount; i++) {
						int thisRoll = this.formula.calculateNewCount(context.getRandom(), stack.getCount(), enchantmentLevel);
						highestRoll = Math.max(highestRoll, thisRoll);
					}
					return highestRoll;
				}
			}
		}
		return oldValue;
	}
	
}
