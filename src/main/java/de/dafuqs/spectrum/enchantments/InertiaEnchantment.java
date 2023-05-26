package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;

public class InertiaEnchantment extends SpectrumEnchantment {
	
	@Unique
	public static final String INERTIA_BLOCK = "Inertia_LastMinedBlock";
	public static final String INERTIA_COUNT = "Inertia_LastMinedBlockCount";
	
	public InertiaEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 10;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.InertiaMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return other != Enchantments.EFFICIENCY && super.canAccept(other);
	}
	
}
