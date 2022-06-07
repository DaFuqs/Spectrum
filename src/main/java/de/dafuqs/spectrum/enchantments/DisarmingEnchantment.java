package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class DisarmingEnchantment extends SpectrumEnchantment {
	
	public DisarmingEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static void disarmPlayer(PlayerEntity player) {
		int equipmentSlotCount = EquipmentSlot.values().length;
		int randomSlot = (int) (Math.random() * equipmentSlotCount);
		int slotsChecked = 0;
		while (slotsChecked < equipmentSlotCount) {
			EquipmentSlot slot = EquipmentSlot.values()[randomSlot];
			ItemStack equippedStack = player.getEquippedStack(slot);
			if (!equippedStack.isEmpty()) {
				player.dropStack(equippedStack);
				player.equipStack(slot, ItemStack.EMPTY);
				player.world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				break;
			}
			
			randomSlot = (randomSlot + 1) % equipmentSlotCount;
			slotsChecked++;
		}
	}
	
	public static void disarmEntity(LivingEntity livingEntity, DefaultedList<ItemStack> syncedArmorStacks) {
		// since endermen save their carried block as blockState, not in hand
		// we have to use custom logic for them
		if (livingEntity instanceof EndermanEntity endermanEntity) {
			BlockState carriedBlockState = endermanEntity.getCarriedBlock();
			if (carriedBlockState != null) {
				Item item = carriedBlockState.getBlock().asItem();
				if (item != null) {
					endermanEntity.dropStack(item.getDefaultStack());
					endermanEntity.setCarriedBlock(null);
				}
			}
			return;
		}
		
		int randomSlot = (int) (Math.random() * 6);
		int slotsChecked = 0;
		while (slotsChecked < 6) {
			if (randomSlot == 5) {
				if (livingEntity.getMainHandStack() != null && !livingEntity.getMainHandStack().isEmpty()) {
					livingEntity.dropStack(livingEntity.getMainHandStack());
					livingEntity.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					livingEntity.world.playSound(null, livingEntity.getBlockPos(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					break;
				}
			} else if (randomSlot == 4) {
				if (livingEntity.getOffHandStack() != null && !livingEntity.getOffHandStack().isEmpty()) {
					livingEntity.dropStack(livingEntity.getOffHandStack());
					livingEntity.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
					livingEntity.world.playSound(null, livingEntity.getBlockPos(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					break;
				}
			} else {
				if (syncedArmorStacks != null && !syncedArmorStacks.get(randomSlot).isEmpty()) {
					livingEntity.dropStack(syncedArmorStacks.get(randomSlot));
					syncedArmorStacks.set(randomSlot, ItemStack.EMPTY);
					livingEntity.world.playSound(null, livingEntity.getBlockPos(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					break;
				}
			}
			
			randomSlot = (randomSlot + 1) % 6;
			slotsChecked++;
		}
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
	
}
