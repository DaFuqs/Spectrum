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

public class ExtraMiningSpeedRingItem extends InkDrainTrinketItem {
	
	public ExtraMiningSpeedRingItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/ring_of_pursuit"), InkColors.MAGENTA);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.ring_of_pursuit.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	public static ResourceLocation MINING_SPEED_ATTRIBUTE_ID = SpectrumCommon.locate("ring_of_pursuit_mining_speed");
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double miningSpeedMod = getExtraMiningSpeed(storedInk);
		if (miningSpeedMod != 0) {
			modifiers.put(Attributes.MINING_EFFICIENCY, new AttributeModifier(MINING_SPEED_ATTRIBUTE_ID, miningSpeedMod, AttributeModifier.Operation.ADD_VALUE));
		}
		
		return modifiers;
	}
	
	public double getExtraMiningSpeed(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 1 + (int) (Math.log(storedInk / 100.0f) / Math.log(8));
		}
	}
	
}
