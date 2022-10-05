package de.dafuqs.spectrum.items.food;

import com.google.common.collect.Lists;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public class InfusedBeverageItem extends BeverageItem {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.2F).snack().build();
	
	public InfusedBeverageItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return VariantBeverageProperties.getFromStack(itemStack);
	}
	
	public static class VariantBeverageProperties extends BeverageProperties {
		
		public List<StatusEffectInstance> statusEffects;
		public final String variant;
		
		public VariantBeverageProperties(long ageDays, int alcPercent, float thickness, String variant) {
			super(ageDays, alcPercent, thickness);
			this.variant = variant;
		}
		
		public VariantBeverageProperties(NbtCompound nbtCompound, String variant) {
			super(nbtCompound);
			this.variant = variant;
			
			this.statusEffects = Lists.newArrayList();
			if (nbtCompound != null) {
				PotionUtil.getCustomPotionEffects(nbtCompound, statusEffects);
			}
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
			PotionUtil.buildTooltip(itemStack, tooltip, 1.0F);
		}
		
		public NbtCompound toNbt(NbtCompound nbtCompound) {
			super.toNbt(nbtCompound);
			nbtCompound.putString("Variant", variant);
			
			NbtList nbtList = nbtCompound.getList("CustomPotionEffects", 9);
			for (StatusEffectInstance statusEffectInstance : this.statusEffects) {
				nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
			}
			nbtCompound.put("CustomPotionEffects", nbtList);
			
			return nbtCompound;
		}
		
	}
	
}
