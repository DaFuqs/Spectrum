package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;

import java.util.*;

public class BeverageItem extends DrinkItem implements FermentedItem {
	
	public BeverageItem(Properties settings) {
		super(settings.component(SpectrumDataComponentTypes.BEVERAGE, BeverageComponent.DEFAULT));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		Player playerEntity = user instanceof Player ? (Player) user : null;
		if (playerEntity instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) playerEntity, stack);
		}
		
		if (!world.isClientSide() && stack.has(DataComponents.POTION_CONTENTS))
			stack.get(DataComponents.POTION_CONTENTS).forEachEffect((effect) -> {
				if ((effect.getEffect().value()).isInstantenous()) {
					(effect.getEffect().value()).applyInstantenousEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
				} else user.addEffect(effect);
			});
		
		if (playerEntity != null) playerEntity.awardStat(Stats.ITEM_USED.get(this));
		
		user.gameEvent(GameEvent.DRINK);
		return super.finishUsingItem(stack, world, user);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		var infused = stack.get(SpectrumDataComponentTypes.INFUSED_BEVERAGE);
		if (infused != null) infused.addToTooltip(context, tooltip::add, type);
		
		var beverage = stack.get(SpectrumDataComponentTypes.BEVERAGE);
		if (beverage != null) beverage.addToTooltip(context, tooltip::add, type);
		
		var jade = stack.get(SpectrumDataComponentTypes.JADE_WINE);
		if (jade != null) jade.addToTooltip(context, tooltip::add, type);
		
		var effects = stack.get(DataComponents.POTION_CONTENTS);
		if (effects != null) effects.addPotionTooltip(tooltip::add, 1.0f, context.tickRate());
	}
	
}
