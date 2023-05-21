package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

// riptide w/o weather requirement; damages enemies on touch; iframes?
public class FerociousBidentItem extends MalachiteBidentItem {
	
	public static final InkCost RIPTIDE_COST = new InkCost(InkColors.WHITE, 10);
	public static final int BUILTIN_RIPTIDE_LEVEL = 3;
	
	public FerociousBidentItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public int getBuiltinRiptideLevel() {
		return BUILTIN_RIPTIDE_LEVEL;
	}
	
	@Override
	public boolean requiresRainForRiptide() {
		return false;
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.usageTick(world, user, stack, remainingUseTicks);
		if (user.isUsingRiptide() && user instanceof PlayerEntity player) {
			if (!InkPowered.tryDrainEnergy(player, RIPTIDE_COST)) {
				return;
			}
			
			int useTime = this.getMaxUseTime(stack) - remainingUseTicks;
			if (useTime % 10 == 0) {
				stack.damage(1, user, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
			}
			yeetPlayer(player, getRiptideLevel(stack) / 10F - 0.75F);
			player.useRiptide(20);
			for (LivingEntity entityAround : world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), player.getBoundingBox().expand(2), LivingEntity::isAlive)) {
				if (entityAround != player) {
					entityAround.damage(DamageSource.player(player), 2);
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
	
}
