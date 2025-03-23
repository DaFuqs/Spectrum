package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public abstract class GravityRingItem extends InkDrainTrinketItem implements GravitableItem {
	
	public GravityRingItem(Settings settings, Identifier unlockIdentifier, InkColor inkColor) {
		super(settings, unlockIdentifier, inkColor);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double knockbackResistance = getBonus(storedInk) / 10D;
		if (knockbackResistance != 0) {
			modifiers.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uuid, getAttributeName(), negativeGravity() ? -knockbackResistance : knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
		}
		
		return modifiers;
	}
	
	protected abstract String getAttributeName();
	
	protected abstract boolean negativeGravity();
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		applyGravity(stack, entity.getWorld(), entity);
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
