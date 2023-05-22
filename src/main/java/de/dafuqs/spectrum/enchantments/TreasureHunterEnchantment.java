package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;

public class TreasureHunterEnchantment extends SpectrumEnchantment {
	
	public TreasureHunterEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static void doTreasureHunterForPlayer(ServerPlayerEntity thisEntity, DamageSource source) {
		if (!thisEntity.isSpectator() && source.getAttacker() instanceof LivingEntity) {
			int damageSourceTreasureHunt = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.TREASURE_HUNTER, (LivingEntity) source.getAttacker());
			if (damageSourceTreasureHunt > 0) {
				ServerWorld serverWorld = thisEntity.getWorld();
				boolean shouldDropHead = serverWorld.getRandom().nextFloat() < 0.2 * damageSourceTreasureHunt;
				if (shouldDropHead) {
					ItemStack headItemStack = new ItemStack(Items.PLAYER_HEAD);
					
					NbtCompound compoundTag = new NbtCompound();
					compoundTag.putString("SkullOwner", thisEntity.getName().getString());
					
					headItemStack.setNbt(compoundTag);
					
					ItemEntity headEntity = new ItemEntity(serverWorld, thisEntity.getX(), thisEntity.getY(), thisEntity.getZ(), headItemStack);
					serverWorld.spawnEntity(headEntity);
				}
			}
		}
	}
	
	@Override
	public int getMinPower(int level) {
		return 15;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.TreasureHunterMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.LOOTING;
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || stack.getItem() instanceof AxeItem || stack.isIn(ConventionalItemTags.AXES);
	}
	
}