package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.category.*;
import net.fabricmc.api.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public class PotionWorkshopBrewingCategory extends PotionWorkshopCategory<PotionWorkshopBrewingDisplay> {
	
	@Override
	public CategoryIdentifier<PotionWorkshopBrewingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_BREWING;
	}
	
	@Override
	public ResourceLocation getIdentifier() {
		return SpectrumCommon.locate("potion_workshop_brewing");
	}
	
	@Override
	public Component getTitle() {
		return Component.translatable("container.spectrum.rei.potion_workshop_brewing.title");
	}
	
}
