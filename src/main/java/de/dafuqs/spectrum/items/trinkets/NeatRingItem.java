package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
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

}