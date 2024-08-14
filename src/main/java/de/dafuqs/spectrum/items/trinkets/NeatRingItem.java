package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.SleepStatusAffectingItem;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.*;

import java.util.*;

public class NeatRingItem extends SpectrumTrinketItem implements SleepStatusAffectingItem {

	public static final float THE_FUNNY = 0.0069F;

	public NeatRingItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/neat_ring"));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.neat_ring.tooltip"));
		tooltip.add(Text.translatable("info.spectrum.tooltip.sleep_resist.positive", StringUtils.left(String.valueOf(THE_FUNNY * 100), 4)).styled(s -> s.withColor(SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR)));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}

	@Override
	public float getSleepResistance(PlayerEntity player, ItemStack stack) {
		return THE_FUNNY;
	}
}