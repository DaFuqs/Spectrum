package de.dafuqs.spectrum.items.food;

import net.minecraft.advancements.*;
import net.minecraft.core.component.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;

public class StatusEffectDrinkItem extends DrinkItem {
	
	public StatusEffectDrinkItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		Player playerEntity = user instanceof Player ? (Player) user : null;
		if (playerEntity instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) playerEntity, stack);
		}
		
		if (!world.isClientSide) {
			PotionContents potionContentsComponent = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
			potionContentsComponent.forEachEffect((effect) -> {
				if ((effect.getEffect().value()).isInstantenous()) {
					(effect.getEffect().value()).applyInstantenousEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
				} else {
					user.addEffect(effect);
				}
			});
		}
		
		if (playerEntity != null) {
			playerEntity.awardStat(Stats.ITEM_USED.get(this));
		}
		
		user.gameEvent(GameEvent.DRINK);
		return super.finishUsingItem(stack, world, user);
	}
	
}
