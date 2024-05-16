package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
import de.dafuqs.spectrum.helpers.ColorHelper;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

// gets thrown as copy instead of getting removed from the player's inv
public class FractalBidentItem extends MalachiteBidentItem implements SlotBackgroundEffectProvider {
	
	public static final InkCost MIRROR_IMAGE_COST = new InkCost(InkColors.WHITE, 25);
	
	public FractalBidentItem(Settings settings, double attackSpeed, double damage, float armorPierce, float protPierce) {
		super(settings, attackSpeed, damage, armorPierce, protPierce);
	}
	
	@Override
	public boolean isThrownAsMirrorImage(ItemStack stack, ServerWorld world, PlayerEntity player) {
		return !isDisabled(stack) && InkPowered.tryDrainEnergy(player, MIRROR_IMAGE_COST);
	}
	
	@Override
	public float getThrowSpeed(ItemStack stack) {
		return isDisabled(stack) ? super.getThrowSpeed(stack) : 5.0F;
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
	public boolean canBeDisabled() {
		return true;
	}
	
	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return super.acceptsEnchantment(enchantment) || enchantment == Enchantments.EFFICIENCY || enchantment == Enchantments.POWER;
	}

	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, MIRROR_IMAGE_COST);
		return usable ? SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE : SlotBackgroundEffectProvider.SlotEffect.NONE;
	}

	@Override
	public float getProtReduction(LivingEntity target, ItemStack stack) {
		return 0.25F;
	}

	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return ColorHelper.colorVecToRGB(InkColors.PURPLE.getColor());
	}
	
}
