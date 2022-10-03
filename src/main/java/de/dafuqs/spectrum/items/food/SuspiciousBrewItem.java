package de.dafuqs.spectrum.items.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

public class SuspiciousBrewItem extends Item {
	
	public SuspiciousBrewItem(Settings settings) {
		super(settings);
	}
	
	public static void addEffectToStew(ItemStack stew, StatusEffect effect, int duration) {
		SuspiciousStewItem.addEffectToStew(stew, effect, duration);
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		NbtCompound nbtCompound = stack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("Effects", 9)) {
			NbtList nbtList = nbtCompound.getList("Effects", 10);
			
			for(int i = 0; i < nbtList.size(); ++i) {
				int j = 160;
				NbtCompound nbtCompound2 = nbtList.getCompound(i);
				if (nbtCompound2.contains("EffectDuration", 3)) {
					j = nbtCompound2.getInt("EffectDuration");
				}
				
				StatusEffect statusEffect = StatusEffect.byRawId(nbtCompound2.getByte("EffectId"));
				if (statusEffect != null) {
					user.addStatusEffect(new StatusEffectInstance(statusEffect, j));
				}
			}
		}
		
		return super.finishUsing(stack, world, user);
	}
	
}
