package de.dafuqs.spectrum.items.food.beverages.properties;

import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class VariantBeverageProperties extends StatusEffectBeverageProperties {
	
	public final String variant;
	
	public VariantBeverageProperties(long ageDays, int alcPercent, float thickness, String variant, List<StatusEffectInstance> statusEffects) {
		super(ageDays, alcPercent, thickness, statusEffects);
		this.variant = variant;
	}
	
	public VariantBeverageProperties(NbtCompound nbtCompound, String variant) {
		super(nbtCompound);
		this.variant = variant;
	}
	
	public static VariantBeverageProperties getFromStack(ItemStack itemStack) {
		String variant;
		
		NbtCompound nbtCompound = itemStack.getNbt();
		variant = nbtCompound != null && nbtCompound.contains("Variant") ? nbtCompound.getString("Variant") : "unknown";
		
		return new VariantBeverageProperties(nbtCompound, variant);
	}
	
	@Override
	public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
		tooltip.add(Text.translatable("item.spectrum.infused_beverage.tooltip.variant." + variant).formatted(Formatting.YELLOW));
		super.addTooltip(itemStack, tooltip);
	}
	
	@Override
	public void toNbt(NbtCompound nbtCompound) {
		super.toNbt(nbtCompound);
		nbtCompound.putString("Variant", variant);
	}
	
}
