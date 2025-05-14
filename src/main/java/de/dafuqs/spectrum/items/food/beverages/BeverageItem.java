package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.items.food.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

import java.util.*;

public class BeverageItem extends StatusEffectDrinkItem implements FermentedItem {
	
	public BeverageItem(Properties settings) {
		super(settings.component(SpectrumDataComponentTypes.BEVERAGE, BeverageComponent.DEFAULT));
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
