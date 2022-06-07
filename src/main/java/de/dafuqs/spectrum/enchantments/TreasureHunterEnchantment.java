package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

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
	
	public int getMinPower(int level) {
		return 15;
	}
	
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.TreasureHunterMaxLevel;
	}
	
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.LOOTING;
	}
	
}