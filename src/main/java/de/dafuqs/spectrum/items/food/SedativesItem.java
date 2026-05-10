package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class SedativesItem extends ItemWithTooltip {
	
	public SedativesItem(Properties settings, String tooltip) {
		super(settings, tooltip);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof ServerPlayer serverPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
			serverPlayer.awardStat(Stats.ITEM_USED.get(this));
		}
		if (!world.isClientSide()) {
			user.removeEffect(SpectrumStatusEffects.FRENZY);
			
			// TODO - Reenable compat when up-to-date
			//if (SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.NEEPMEAT_ID)) {
			//	NEEPMeatCompat.sedateEnlightenment(user);
			//}
		}
		
		return super.finishUsingItem(stack, world, user);
	}
	
}
