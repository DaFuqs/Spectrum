package de.dafuqs.spectrum.items.trinkets;

import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public abstract class AzureDikeTrinketItem extends SpectrumTrinketItem implements AzureDikeItem {
	
	public AzureDikeTrinketItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		recalculate(entity);
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onUnequip(stack, slot, entity);
		recalculate(entity);
	}
	
	@Override
	public void onBreak(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onBreak(stack, slot, entity);
		recalculate(entity);
	}
	
}