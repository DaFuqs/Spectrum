package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class DisarmingEnchantment extends SpectrumEnchantment {

	public DisarmingEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public int getMinPower(int level) {
		return 10;
	}

	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}

	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.DisarmingMaxLevel;
	}

	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
	
	
	public static void doDisarmingEffect(PlayerEntity player) {
		int randomSlot = (int) (Math.random() * 6);
		int slotsChecked = 0;
		while (slotsChecked < 6) {
			if(randomSlot == 5) {
				if(player.getMainHandStack() != null) {
					player.dropStack(player.getMainHandStack());
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					break;
				}
			} else if(randomSlot == 4) {
				if(player.getOffHandStack() != null) {
					player.dropStack(player.getOffHandStack());
					player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
					break;
				}
			} else {
				ItemStack armorStack = player.getInventory().armor.get(randomSlot);
				if(!armorStack.isEmpty()) {
					player.dropStack(armorStack);
					player.getInventory().armor.set(randomSlot, ItemStack.EMPTY);
					break;
				}
			}
			
			randomSlot = (randomSlot + 1) % 6;
			slotsChecked++;
		}
	}
	
}
