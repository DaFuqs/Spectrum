package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ThreatConfluxItem extends ModularExplosionBlockItem {
	
	public ThreatConfluxItem(Block block, Settings settings) {
		super(block, 5, 20, 5, settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("block.spectrum.threat_conflux.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("block.spectrum.threat_conflux.tooltip2").formatted(Formatting.GRAY).append(SpectrumItems.MIDNIGHT_CHIP.getName()));
		super.appendTooltip(stack, world, tooltip, context);
	}
	
}
