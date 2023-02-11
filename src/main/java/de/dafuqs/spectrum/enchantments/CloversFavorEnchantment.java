package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.SpectrumEnchantmentHelper;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class CloversFavorEnchantment extends SpectrumEnchantment {
	
	public CloversFavorEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static float rollChance(float baseChance, Entity entity) {
		if(entity instanceof LivingEntity livingEntity) {
			int rareLootLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.CLOVERS_FAVOR, livingEntity.getMainHandStack(), entity);
			if (rareLootLevel > 0) {
				return baseChance * (float) rareLootLevel * rareLootLevel;
			}
		}
		return 0;
	}
	
	@Override
	public int getMinPower(int level) {
		return 20;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.CloversFavorMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
	
	
}
