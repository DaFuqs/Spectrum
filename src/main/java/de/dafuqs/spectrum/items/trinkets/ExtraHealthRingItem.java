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

public class ExtraHealthRingItem extends InkDrainTrinketItem {
	
	public ExtraHealthRingItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/heartsingers_reward"), InkColors.PINK);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.heartsingers_reward.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	public static ResourceLocation HEALTH_ATTRIBUTE_ID = SpectrumCommon.locate("heartsingers_reward_health");
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		int extraHearts = getExtraHearts(storedInk);
		if (extraHearts != 0) {
			modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(HEALTH_ATTRIBUTE_ID, extraHearts, AttributeModifier.Operation.ADD_VALUE));
		}
		
		return modifiers;
	}
	
	public int getExtraHearts(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 2 + 2 * (int) (Math.log(storedInk / 100.0f) / Math.log(8));
		}
	}
	
}
