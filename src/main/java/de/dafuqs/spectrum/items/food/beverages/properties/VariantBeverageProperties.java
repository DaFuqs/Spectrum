package de.dafuqs.spectrum.items.food.beverages.properties;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

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
	
	public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
		tooltip.add(new TranslatableText("item.spectrum.infused_beverage.tooltip.variant." + variant).formatted(Formatting.YELLOW));
		super.addTooltip(itemStack, tooltip);
	}
	
	public NbtCompound toNbt(NbtCompound nbtCompound) {
		super.toNbt(nbtCompound);
		nbtCompound.putString("Variant", variant);
		return nbtCompound;
	}
	
}
