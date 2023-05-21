package de.dafuqs.spectrum.items.food.beverages.properties;

import com.google.common.collect.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.text.*;

import java.util.*;

public class StatusEffectBeverageProperties extends BeverageProperties {
	
	public List<StatusEffectInstance> statusEffects;
	
	public StatusEffectBeverageProperties(long ageDays, int alcPercent, float thickness, List<StatusEffectInstance> statusEffects) {
		super(ageDays, alcPercent, thickness);
		this.statusEffects = statusEffects;
	}
	
	public StatusEffectBeverageProperties(NbtCompound nbtCompound) {
		super(nbtCompound);
		
		this.statusEffects = Lists.newArrayList();
		if (nbtCompound != null) {
			PotionUtil.getCustomPotionEffects(nbtCompound, statusEffects);
		}
	}
	
	public static StatusEffectBeverageProperties getFromStack(ItemStack itemStack) {
		return new StatusEffectBeverageProperties(itemStack.getNbt());
	}
	
	@Override
	public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
		super.addTooltip(itemStack, tooltip);
		PotionUtil.buildTooltip(itemStack, tooltip, 1.0F);
	}
	
	@Override
	public void toNbt(NbtCompound nbtCompound) {
		super.toNbt(nbtCompound);
		
		NbtList nbtList = nbtCompound.getList("CustomPotionEffects", 9);
		for (StatusEffectInstance statusEffectInstance : this.statusEffects) {
			nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
		}
		nbtCompound.put("CustomPotionEffects", nbtList);
		
	}
}
