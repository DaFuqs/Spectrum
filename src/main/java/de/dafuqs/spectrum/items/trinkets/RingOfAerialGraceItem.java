package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
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

public class RingOfAerialGraceItem extends InkDrainTrinketItem implements GravitableItem {

	public RingOfAerialGraceItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/ring_of_aerial_grace"), InkColors.WHITE);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.ring_of_aerial_grace.tooltip").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);

		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double knockbackResistance = getBonus(storedInk);
		if (knockbackResistance != 0) {
			modifiers.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uuid, "spectrum:ring_of_aerial_grace", knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
		}

		return modifiers;
	}

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
		return -((float) getBonus(storedInk) / 100);
	}

}
