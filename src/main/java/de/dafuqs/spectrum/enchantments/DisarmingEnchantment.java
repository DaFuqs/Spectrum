package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;

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
				ItemEntity itemEntity = new ItemEntity(player.world, player.getX(), player.getY(), player.getZ(), equippedStack);
				itemEntity.setVelocity(player.world.random.nextTriangular(0.0, 0.11485000171139836), player.world.random.nextTriangular(0.2, 0.11485000171139836), player.world.random.nextTriangular(0.0, 0.11485000171139836));
				itemEntity.setPickupDelay(120);
				player.world.spawnEntity(itemEntity);
				
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
		return SpectrumCommon.CONFIG.DisarmingMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
	
}
