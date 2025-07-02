package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import dev.emi.trinkets.api.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;

import java.util.*;

public class ExtraReachGlovesItem extends InkDrainTrinketItem {
	
	public ExtraReachGlovesItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/gloves_of_dawns_grasp"), InkColors.LIGHT_BLUE);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.gloves_of_dawns_grasp.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	public static ResourceLocation BLOCK_INTERACTION_ATTRIBUTE_ID = SpectrumCommon.locate("gloves_of_dawns_grasp_block_interaction");
	public static ResourceLocation ENTITY_INTERACTION_ATTRIBUTE_ID = SpectrumCommon.locate("gloves_of_dawns_grasp_entity_interaction");
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double extraReach = getExtraReach(storedInk);
		if (extraReach != 0) {
			modifiers.put(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(BLOCK_INTERACTION_ATTRIBUTE_ID, extraReach, AttributeModifier.Operation.ADD_VALUE));
			modifiers.put(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(ENTITY_INTERACTION_ATTRIBUTE_ID, extraReach / 6, AttributeModifier.Operation.ADD_VALUE));
		}
		
		return modifiers;
	}
	
	public double getExtraReach(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 0.5 + roundHalf(Math.log(storedInk / 100.0f) / Math.log(64));
		}
	}
	
	public static double roundHalf(double number) {
		return ((int) (number * 2)) / 2D;
	}
	
}
