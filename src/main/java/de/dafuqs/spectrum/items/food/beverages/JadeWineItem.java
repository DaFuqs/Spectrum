package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;

import java.util.*;

public class JadeWineItem extends BeverageItem {
	
	public JadeWineItem(Properties settings) {
		super(settings.component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).component(SpectrumDataComponentTypes.JADE_WINE, JadeWineComponent.DEFAULT));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		if (FermentedItem.isPreviewStack(stack)) {
			String translationKey = getDescriptionId();
			tooltip.add(Component.translatable(translationKey + ".tooltip.preview").withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable(translationKey + ".tooltip.preview2").withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable("item.spectrum.tooltip.could_use_some_sweetener").withStyle(ChatFormatting.GRAY));
		}
	}
	
}
