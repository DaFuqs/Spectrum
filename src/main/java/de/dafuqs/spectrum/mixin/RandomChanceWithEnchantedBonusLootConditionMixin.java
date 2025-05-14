package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LootItemRandomChanceWithEnchantedBonusCondition.class)
public abstract class RandomChanceWithEnchantedBonusLootConditionMixin {
	
	@Shadow
	@Final
	private LevelBasedValue enchantedChance;
	
	@Shadow
	@Final
	private Holder<Enchantment> enchantment;
	
	@ModifyReturnValue(at = @At("RETURN"), method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z")
	public boolean spectrum$applyRareLootEnchantment(boolean original, LootContext context) {
		// if the result was to not drop a drop before reroll
		// gets more probable with each additional level of Clovers Favor
		if (!original) {
			// TODO: can we use localcapture here to avoid recalculating these values?
			if (context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity livingEntity) {
				int level = EnchantmentHelper.getEnchantmentLevel(this.enchantment, livingEntity);
				if (level > 0) {
					float enchantedChanceValue = this.enchantedChance.calculate(level);
					original = context.getRandom().nextFloat() < CloversFavorHelper.rollChance(enchantedChanceValue, context.getParamOrNull(LootContextParams.ATTACKING_ENTITY));
				}
			}
		}
		return original;
	}
	
}
