package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

// gets thrown as copy instead of getting removed from the player's inv
public class FractalBidentItem extends MalachiteBidentItem {
	
	public static final InkCost MIRROR_IMAGE_COST = new InkCost(InkColors.WHITE, 25);
	
	public FractalBidentItem(Settings settings, double damage) {
		super(settings, damage);
	}
	
	@Override
	public boolean isThrownAsMirrorImage(ItemStack stack, ServerWorld world, PlayerEntity player) {
		return InkPowered.tryDrainEnergy(player, MIRROR_IMAGE_COST);
	}
	
	@Override
	public float getThrowSpeed() {
		return 5.0F;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.fractal_glass_crest_bident.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.fractal_glass_crest_bident.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.fractal_glass_crest_bident.tooltip3").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.white").formatted(Formatting.GRAY));
	}
	
	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return super.acceptsEnchantment(enchantment) || enchantment == Enchantments.EFFICIENCY || enchantment == Enchantments.POWER;
	}
	
}
