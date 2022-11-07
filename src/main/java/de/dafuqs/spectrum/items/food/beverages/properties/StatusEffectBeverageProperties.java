package de.dafuqs.spectrum.items.food.beverages.properties;

import com.google.common.collect.Lists;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;

import java.util.List;

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
	
	public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
		super.addTooltip(itemStack, tooltip);
		PotionUtil.buildTooltip(itemStack, tooltip, 1.0F);
	}
	
	public NbtCompound toNbt(NbtCompound nbtCompound) {
		super.toNbt(nbtCompound);
		
		NbtList nbtList = nbtCompound.getList("CustomPotionEffects", 9);
		for (StatusEffectInstance statusEffectInstance : this.statusEffects) {
			nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
		}
		nbtCompound.put("CustomPotionEffects", nbtList);
		
		return nbtCompound;
	}
}
