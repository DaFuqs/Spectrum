package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NeatRingItem extends SpectrumTrinketItem {


	public NeatRingItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/neat_ring"));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.neat_ring.tooltip"));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		modifiers.put(SpectrumEntityAttributes.INDUCED_SLEEP_VULNERABILITY, new EntityAttributeModifier(uuid, "spectrum:neat_ring", -0.0069, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		return modifiers;
	}
	
}