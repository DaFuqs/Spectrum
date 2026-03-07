package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;

public abstract class GravityRingItem extends InkDrainCurioItem {
	
	public GravityRingItem(Properties settings, ResourceLocation unlockIdentifier, InkColor inkColor) {
		super(settings, unlockIdentifier, inkColor);
	}
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getAttributeModifiers(slotContext, id, stack);
		
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
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		super.curioTick(slotContext, stack);
		
		float gravityMod = getGravityMod(stack);
		if(gravityMod != 0) {
			LivingEntity entity = slotContext.entity();
			entity.push(0, gravityMod, 0);
		
			// if falling very slowly => reset fall distance / damage
			if (entity.getDeltaMovement().y > -0.4) {
				entity.fallDistance = 0;
			}
		}
	}
	
	public float getGravityMod(ItemStack stack) {
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		float bonus = ((float) getBonus(storedInk)) / 100F;
		return negativeGravity() ? bonus : -bonus;
	}
	
	public double getBonus(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 1 + (int) (Math.log(storedInk / 100.0f) / Math.log(8));
		}
	}
	
}
