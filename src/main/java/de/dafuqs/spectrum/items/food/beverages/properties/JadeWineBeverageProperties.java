package de.dafuqs.spectrum.items.food.beverages.properties;

import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class JadeWineBeverageProperties extends StatusEffectBeverageProperties {
	
	public final float bloominess;
	public final boolean sweetened;
	
	public JadeWineBeverageProperties(long ageDays, int alcPercent, float thickness, float bloominess, boolean sweetened, List<StatusEffectInstance> statusEffects) {
		super(ageDays, alcPercent, thickness, statusEffects);
		this.bloominess = bloominess;
		this.sweetened = sweetened;
	}
	
	public JadeWineBeverageProperties(NbtCompound nbtCompound, float bloominess, boolean sweetened) {
		super(nbtCompound);
		this.bloominess = bloominess;
		this.sweetened = sweetened;
	}
	
	public static JadeWineBeverageProperties getFromStack(ItemStack itemStack) {
		float bloominess = 0;
		boolean sweetened = false;
		
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null) {
			bloominess = nbtCompound.contains("Bloominess") ? nbtCompound.getFloat("Bloominess") : 0;
			sweetened = nbtCompound.contains("Sweetened") && nbtCompound.getBoolean("Sweetened");
		}
		
		return new JadeWineBeverageProperties(nbtCompound, bloominess, sweetened);
	}
	
	@Override
	public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
		tooltip.add(Text.translatable("item.spectrum.infused_beverage.tooltip.age", ageDays, alcPercent).formatted(Formatting.GRAY));
		if (sweetened) {
			tooltip.add(Text.translatable("item.spectrum.jade_wine.tooltip.bloominess_sweetened", bloominess).formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.translatable("item.spectrum.jade_wine.tooltip.bloominess", bloominess).formatted(Formatting.GRAY));
		}
		PotionUtil.buildTooltip(itemStack, tooltip, 1.0F);
	}
	
	@Override
	public void toNbt(NbtCompound nbtCompound) {
		super.toNbt(nbtCompound);
		nbtCompound.putFloat("Bloominess", bloominess);
		nbtCompound.putBoolean("Sweetened", sweetened);
	}
	
}
	