package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

// riptide w/o weather requirement; damages enemies on touch; iframes?
public class FerociousBidentItem extends MalachiteBidentItem implements SlotBackgroundEffectProvider, InkPowered {
	
	public static final InkCost RIPTIDE_COST = new InkCost(InkColors.WHITE, 10);
	public static final int BUILTIN_RIPTIDE_LEVEL = 1;
	
	public FerociousBidentItem(Item.Properties settings, double attackSpeed, double damage, float armorPierce, float protPierce) {
		super(settings, attackSpeed, damage, armorPierce, protPierce);
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(RIPTIDE_COST.color());
	}
	
	@Override
	public int getRiptideLevel(HolderLookup.Provider lookup, ItemStack stack) {
		return Math.max(SpectrumEnchantmentHelper.getLevel(lookup, Enchantments.RIPTIDE, stack), BUILTIN_RIPTIDE_LEVEL);
	}

	@Override
	public boolean canStartRiptide(Player player, ItemStack stack) {
		return !isDisabled(stack) && (super.canStartRiptide(player, stack) || InkPowered.tryDrainEnergy(player, RIPTIDE_COST));
	}
	
	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.onUseTick(world, user, stack, remainingUseTicks);
		if (user.isAutoSpinAttack() && user instanceof Player player) {
			
			int useTime = this.getUseDuration(stack, user) - remainingUseTicks;
			if (useTime % 10 == 0) {
				if (InkPowered.tryDrainEnergy(player, RIPTIDE_COST)) {
					stack.hurtAndBreak(1, user, LivingEntity.getSlotForHand(user.getUsedItemHand()));
				} else {
					user.releaseUsingItem();
					return;
				}
			}
			
			yeetPlayer(player, getRiptideLevel(world.registryAccess(), stack) / 128F - 0.75F);
			player.startAutoSpinAttack(20, 12.0F, stack);
			
			for (LivingEntity entityAround : world.getEntities(EntityTypeTest.forClass(LivingEntity.class), player.getBoundingBox().inflate(2), LivingEntity::isAlive)) {
				if (entityAround != player) {
					entityAround.hurt(world.damageSources().playerAttack(player), 2);
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.ferocious_glass_crest_bident.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.ferocious_glass_crest_bident.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.ferocious_glass_crest_bident.tooltip3").withStyle(ChatFormatting.GRAY));
		addInkPoweredTooltip(tooltip);
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
	
	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, RIPTIDE_COST);
		return usable ? SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE : SlotBackgroundEffectProvider.SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return InkColors.ORANGE_COLOR;
	}
	
	@Override
	public float getDefenseMultiplier(LivingEntity target, ItemStack stack) {
		return 0.66F;
	}
	
	@Override
	public float getProtReduction(LivingEntity target, ItemStack stack) {
		return 0.33F;
	}
}
