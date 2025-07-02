package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

public class PotionWorkshopCraftingCategory extends PotionWorkshopCategory<PotionWorkshopCraftingDisplay> {
	
	@Override
	public CategoryIdentifier<PotionWorkshopCraftingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public ResourceLocation getIdentifier() {
		return SpectrumCommon.locate("potion_workshop_crafting");
	}
	
	@Override
	public Component getTitle() {
		return Component.translatable("container.spectrum.rei.potion_workshop_crafting.title");
	}
	
}
