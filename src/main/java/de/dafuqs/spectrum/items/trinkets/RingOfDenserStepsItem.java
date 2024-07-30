package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RingOfDenserStepsItem extends GravityRingItem implements GravitableItem {

	public RingOfDenserStepsItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/ring_of_denser_steps"), InkColors.BROWN);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.ring_of_denser_steps.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.ring_of_denser_steps.tooltip2").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	protected String getAttributeName() {
		return "spectrum:ring_of_denser_steps";
	}
	
	@Override
	protected boolean negativeGravity() {
		return false;
	}

}
