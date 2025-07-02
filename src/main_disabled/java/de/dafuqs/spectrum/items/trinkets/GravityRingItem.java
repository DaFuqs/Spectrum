package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import dev.emi.trinkets.api.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;

public abstract class GravityRingItem extends InkDrainTrinketItem implements GravitableItem {
	
	public GravityRingItem(Properties settings, ResourceLocation unlockIdentifier, InkColor inkColor) {
		super(settings, unlockIdentifier, inkColor);
	}
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double knockbackResistance = getBonus(storedInk) / 10D;
		if (knockbackResistance != 0) {
			modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(getAttributeID(), negativeGravity() ? -knockbackResistance : knockbackResistance, AttributeModifier.Operation.ADD_VALUE));
		}
		
		return modifiers;
	}
	
	protected abstract ResourceLocation getAttributeID();
	
	protected abstract boolean negativeGravity();
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		applyGravity(stack, entity.level(), entity);
	}
	
	public double getBonus(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 1 + (int) (Math.log(storedInk / 100.0f) / Math.log(8));
		}
	}
	
	@Override
	public float getGravityMod(ItemStack stack) {
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		float bonus = ((float) getBonus(storedInk)) / 100F;
		return negativeGravity() ? bonus : -bonus;
	}
	
}
