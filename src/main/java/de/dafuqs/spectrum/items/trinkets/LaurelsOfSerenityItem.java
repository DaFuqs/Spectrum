package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class LaurelsOfSerenityItem extends InkDrainCurioItem {
	
	public LaurelsOfSerenityItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/laurels_of_serenity"), InkColors.PURPLE);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.laurels_of_serenity.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	public static ResourceLocation DETECTION_RANGE_ATTRIBUTE_ID = SpectrumCommon.locate("laurels_of_serenity_detection_range");
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getAttributeModifiers(slotContext, id, stack);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double detectionRangeMod = getDetectionRangeMultiplier(storedInk);
		if (detectionRangeMod != 0) {
			// For some weird reason, Pug, who PRd the attribute to Additional Entity Attributes
			// made negative values be the 'good' variant (aka reducing the distance mobs need to be in to detect an entity)
			// so it shows up red in tooltips. Hmmmm
			modifiers.put(AdditionalEntityAttributes.MOB_DETECTION_RANGE, new AttributeModifier(DETECTION_RANGE_ATTRIBUTE_ID, -detectionRangeMod, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
		return modifiers;
	}
	
	public float getDetectionRangeMultiplier(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 0.15F * (int) (Math.log(storedInk / 100.0f) / Math.log(8)); //TODO: reduce once the ink trinket cap is set back to 1.6 billion
		}
	}
	
}
