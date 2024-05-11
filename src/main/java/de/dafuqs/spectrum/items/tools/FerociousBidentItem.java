package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
import de.dafuqs.spectrum.helpers.ColorHelper;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

// riptide w/o weather requirement; damages enemies on touch; iframes?
public class FerociousBidentItem extends MalachiteBidentItem implements SlotBackgroundEffectProvider {
	
	public static final InkCost RIPTIDE_COST = new InkCost(InkColors.WHITE, 10);
	public static final int BUILTIN_RIPTIDE_LEVEL = 1;

	public FerociousBidentItem(Settings settings, double attackSpeed, double damage, float armorPierce, float protPierce) {
		super(settings, attackSpeed, damage, armorPierce, protPierce);
	}
	
	@Override
	public int getRiptideLevel(ItemStack stack) {
		return Math.max(EnchantmentHelper.getRiptide(stack), BUILTIN_RIPTIDE_LEVEL);
	}

	@Override
	public boolean canStartRiptide(PlayerEntity player, ItemStack stack) {
		return !isDisabled(stack) && (super.canStartRiptide(player, stack) || InkPowered.tryDrainEnergy(player, RIPTIDE_COST));
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.usageTick(world, user, stack, remainingUseTicks);
		if (user.isUsingRiptide() && user instanceof PlayerEntity player) {
			
			int useTime = this.getMaxUseTime(stack) - remainingUseTicks;
			if (useTime % 10 == 0) {
				if (InkPowered.tryDrainEnergy(player, RIPTIDE_COST)) {
					stack.damage(1, user, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
				} else {
					user.stopUsingItem();
					return;
				}
			}
			
			yeetPlayer(player, getRiptideLevel(stack) / 128F - 0.75F);
			player.useRiptide(20);
			
			for (LivingEntity entityAround : world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), player.getBoundingBox().expand(2), LivingEntity::isAlive)) {
				if (entityAround != player) {
					entityAround.damage(world.getDamageSources().playerAttack(player), 2);
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.ferocious_glass_crest_bident.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.ferocious_glass_crest_bident.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.ferocious_glass_crest_bident.tooltip3").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.white").formatted(Formatting.GRAY));
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}

	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, RIPTIDE_COST);
		return usable ? SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE : SlotBackgroundEffectProvider.SlotEffect.NONE;
	}

	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return ColorHelper.colorVecToRGB(InkColors.ORANGE.getColor());
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
