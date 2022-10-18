package de.dafuqs.spectrum.blocks.rock_candy;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RockCandyItem extends Item implements RockCandy {
	
	protected final RockCandyVariant rockCandyVariant;
	
	public RockCandyItem(Settings settings, RockCandyVariant rockCandyVariant) {
		super(settings);
		this.rockCandyVariant = rockCandyVariant;
	}
	
	public RockCandyVariant getVariant() {
		return rockCandyVariant;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("spectrum.block.rock_candy." + this.rockCandyVariant + ".tooltip"));
	}
}
