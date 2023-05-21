package de.dafuqs.spectrum.compat.REI;

import me.shedaniel.rei.api.common.display.*;
import me.shedaniel.rei.api.common.plugins.*;
import me.shedaniel.rei.api.common.transfer.info.*;

public class REICommonIntegration implements REIServerPlugin {
	
	// For shift-clicking into crafting gui
	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		//registry.register(SpectrumPlugins.PEDESTAL_CRAFTING, PedestalCraftingDisplaySerializer.serializer());
	}
	
	@Override
	public void registerMenuInfo(MenuInfoRegistry registry) {
		/*registry.register(BuiltinPlugin.CRAFTING, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
		registry.register(BuiltinPlugin.CRAFTING, CraftingTabletScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
		registry.register(SpectrumPlugins.PEDESTAL_CRAFTING, CraftingTabletScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
		registry.register(SpectrumPlugins.PEDESTAL_CRAFTING, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));*/
	}
	
}