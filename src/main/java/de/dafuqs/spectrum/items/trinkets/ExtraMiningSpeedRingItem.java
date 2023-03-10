package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.energy.storage.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExtraMiningSpeedRingItem extends InkDrainTrinketItem {
	
	public ExtraMiningSpeedRingItem(Settings settings) {
		super(settings, SpectrumCommon.locate("progression/unlock_ring_of_pursuit"), InkColors.MAGENTA);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.ring_of_pursuit.tooltip").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double miningSpeedMod = getExtraMiningSpeed(storedInk);
		if (miningSpeedMod != 0) {
			modifiers.put(AdditionalEntityAttributes.DIG_SPEED, new EntityAttributeModifier(uuid, "spectrum:ring_of_pursuit", miningSpeedMod, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		}
		
		return modifiers;
	}
	
	public double getExtraMiningSpeed(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 0.1 + 0.1 * (int) (Math.log(storedInk / 100) / Math.log(8));
		}
	}
	
}