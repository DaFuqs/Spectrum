package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.Support;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract class SpectrumTrinketItem extends TrinketItem {
	
	public SpectrumTrinketItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if(entity instanceof PlayerEntity playerEntity) {
			if(Support.hasAdvancement(playerEntity, getUnlockIdentifier())) {
				return super.canEquip(stack, slot, entity);
			}
		}
		return false;
	}
	
	protected abstract Identifier getUnlockIdentifier();
	
}
