package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public class DefaultBeverageItem extends BeverageItem {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.2F).snack().build();
	
	public static class DefaultBeverageProperties extends BeverageProperties {
		
		public final String variant;
		
		public DefaultBeverageProperties(NbtCompound nbtCompound, String variant) {
			super(nbtCompound);
			this.variant = variant;
		}
		
		public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
			tooltip.add(new TranslatableText("item.spectrum.beverage.tooltip.variant." + variant).formatted(Formatting.GRAY));
			super.addTooltip(itemStack, tooltip);
		}
		
		public static DefaultBeverageProperties getFromStack(ItemStack itemStack) {
			String variant;
			
			NbtCompound nbtCompound = itemStack.getNbt();
			variant = nbtCompound != null && nbtCompound.contains("Variant") ? nbtCompound.getString("Variant") : "unknown";
			
			return new DefaultBeverageProperties(nbtCompound, variant);
		}
		
		public NbtCompound toNbt(NbtCompound nbtCompound) {
			super.toNbt(nbtCompound);
			nbtCompound.putString("Variant", variant);
			return nbtCompound;
		}
		
		public ItemStack getStack() {
			return super.getStack(SpectrumItems.BEVERAGE.getDefaultStack());
		}
		
	}
	
	public DefaultBeverageItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return DefaultBeverageProperties.getFromStack(itemStack);
	}
	
}
