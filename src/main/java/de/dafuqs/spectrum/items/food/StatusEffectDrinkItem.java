package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.trinkets.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.server.network.*;
import net.minecraft.stat.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

import java.util.*;

public class StatusEffectDrinkItem extends DrinkItem {
	
	public StatusEffectDrinkItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
		}
		
		if (!world.isClient) {
			List<StatusEffectInstance> list = PotionUtil.getPotionEffects(stack);
			for (StatusEffectInstance statusEffectInstance : list) {
				if (statusEffectInstance.getEffectType().isInstant()) {
					statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0D);
				} else {
					user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
				}
			}
		}
		
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		
		user.emitGameEvent(GameEvent.DRINK);
		return super.finishUsing(stack, world, user);
	}
	
}
