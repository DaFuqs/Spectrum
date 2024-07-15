package de.dafuqs.spectrum.items.food;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FissurePlumItem extends AliasedBlockItem {
	
	public FissurePlumItem(Block block, Item.Settings settings) {
		super(block, settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.fissure_plum.tooltip").formatted(Formatting.GRAY));
	}
	
}
