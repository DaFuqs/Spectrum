package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;

import java.util.*;

public class LaurelsOfSerenityItem extends InkDrainTrinketItem {
	
	public LaurelsOfSerenityItem(Properties settings) {
        super(settings, SpectrumCommon.locate("unlocks/trinkets/laurels_of_serenity"), InkColors.PURPLE);
    }

    @Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.laurels_of_serenity.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
    }
	
	public static ResourceLocation DETECTION_RANGE_ATTRIBUTE_ID = SpectrumCommon.locate("laurels_of_serenity_detection_range");
	public static ResourceLocation MENTAL_PRESENCE_ATTRIBUTE_ID = SpectrumCommon.locate("laurels_of_serenity_mental_presence");
    
    @Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
        
        FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
        long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
        double detectionRangeMod = getDetectionRangeMultiplier(storedInk);
        if (detectionRangeMod != 0) {
            // For some weird reason, Pug, who PRd the attribute to Additional Entity Attributes
            // made negative values be the 'good' variant (aka reducing the distance mobs need to be in to detect an entity)
            // so it shows up red in tooltips. Hmmmm
			modifiers.put(AdditionalEntityAttributes.MOB_DETECTION_RANGE, new AttributeModifier(DETECTION_RANGE_ATTRIBUTE_ID, -detectionRangeMod, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        double sleepResistanceMod = getInducedSleepResistanceMod(storedInk);
        if (sleepResistanceMod != 0) {
			modifiers.put(SpectrumEntityAttributes.MENTAL_PRESENCE, new AttributeModifier(MENTAL_PRESENCE_ATTRIBUTE_ID, sleepResistanceMod, AttributeModifier.Operation.ADD_VALUE));
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
    
    public float getInducedSleepResistanceMod(long storedInk) {
        if (storedInk < 100) {
            return 0;
        } else {
            return 0.15F * (int) (Math.log(storedInk / 100.0f) / Math.log(8)); //TODO: reduce once the ink trinket cap is set back to 1.6 billion
        }
    }

}
