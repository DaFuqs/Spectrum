package de.dafuqs.spectrum.items.trinkets;

import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PuffCircletItem extends AzureDikeTrinketItem {

	public static final float PROJECTILE_DEFLECTION_COST = 2;
	public static final float FALL_DAMAGE_NEGATING_COST = 2;

	public PuffCircletItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.puff_circlet.tooltip"));
		tooltip.add(Text.translatable("item.spectrum.puff_circlet.tooltip2"));
	}

	@Override
	public int maxAzureDike(ItemStack stack) {
		return 4;
	}
	
}