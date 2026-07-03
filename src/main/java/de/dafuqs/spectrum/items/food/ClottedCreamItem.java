package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.*;
import net.minecraft.advancements.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.common.*;

public class ClottedCreamItem extends ItemWithTooltip {
	
	public ClottedCreamItem(Properties settings, String[] tooltips) {
		super(settings, tooltips);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof ServerPlayer serverplayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
			serverplayer.awardStat(Stats.ITEM_USED.get(this));
		}
		
		if (!world.isClientSide) {
			user.removeEffectsCuredBy(EffectCures.MILK);
		}
		
		stack.consume(1, user);
		return stack;
	}
	
}
