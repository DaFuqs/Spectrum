package de.dafuqs.spectrum.items.beverages;

import de.dafuqs.spectrum.items.beverages.properties.BeverageProperties;
import de.dafuqs.spectrum.items.beverages.properties.StatusEffectBeverageProperties;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class JadeWineItem extends BeverageItem {
	
	public JadeWineItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return JadeWineBeverageProperties.getFromStack(itemStack);
	}
	
	public static class JadeWineBeverageProperties extends StatusEffectBeverageProperties {
		
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
		
		public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
			super.addTooltip(itemStack, tooltip);
			tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.bloominess", bloominess).formatted(Formatting.GRAY));
			if (sweetened) {
				tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.sweetened").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
			}
		}
		
		public NbtCompound toNbt(NbtCompound nbtCompound) {
			super.toNbt(nbtCompound);
			nbtCompound.putFloat("Bloominess", bloominess);
			nbtCompound.putBoolean("Sweetened", sweetened);
			return nbtCompound;
		}
		
		public ItemStack getStack() {
			return super.getStack(SpectrumItems.JADE_WINE.getDefaultStack());
		}
		
	}
	
}
