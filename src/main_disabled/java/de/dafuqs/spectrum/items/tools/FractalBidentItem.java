package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.render.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;

// gets thrown as copy instead of getting removed from the player's inv
public class FractalBidentItem extends MalachiteBidentItem implements SlotBackgroundEffectProvider, InkPowered {
	
	public static final InkCost MIRROR_IMAGE_COST = new InkCost(InkColors.WHITE, 25);
	
	public FractalBidentItem(Item.Properties settings, double attackSpeed, double damage, float armorPierce, float protPierce) {
		super(settings, attackSpeed, damage, armorPierce, protPierce);
	}
	
	@Override
	public boolean isThrownAsMirrorImage(ItemStack stack, ServerLevel world, Player player) {
		return !isDisabled(stack) && InkPowered.tryDrainEnergy(player, MIRROR_IMAGE_COST);
	}
	
	@Override
	public float getThrowSpeed(ItemStack stack) {
		return isDisabled(stack) ? super.getThrowSpeed(stack) : 5.0F;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(MIRROR_IMAGE_COST.color());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.fractal_glass_crest_bident.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.fractal_glass_crest_bident.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.fractal_glass_crest_bident.tooltip3").withStyle(ChatFormatting.GRAY));
		addInkPoweredTooltip(tooltip);
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
	
	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, MIRROR_IMAGE_COST);
		return usable ? SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE : SlotBackgroundEffectProvider.SlotEffect.NONE;
	}
	
	@Override
	public float getProtReduction(LivingEntity target, ItemStack stack) {
		return 0.25F;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return InkColors.PURPLE_COLOR;
	}
	
	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(Enchantments.EFFICIENCY) || enchantment.is(Enchantments.POWER);
	}
	
}
