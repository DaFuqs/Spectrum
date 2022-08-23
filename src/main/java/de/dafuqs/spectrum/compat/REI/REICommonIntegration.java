package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.compat.REI.plugins.PedestalCraftingDisplaySerializer;
import de.dafuqs.spectrum.inventories.CraftingTabletScreenHandler;
import de.dafuqs.spectrum.inventories.PedestalScreenHandler;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.RecipeBookGridMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

public class REICommonIntegration implements REIServerPlugin {
	
	// For shift-clicking into crafting gui
	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		registry.register(SpectrumPlugins.PEDESTAL_CRAFTING, PedestalCraftingDisplaySerializer.serializer());
	}
	
	@Override
	public void registerMenuInfo(MenuInfoRegistry registry) {
		registry.register(BuiltinPlugin.CRAFTING, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
		registry.register(BuiltinPlugin.CRAFTING, CraftingTabletScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
		registry.register(SpectrumPlugins.PEDESTAL_CRAFTING, CraftingTabletScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
		registry.register(SpectrumPlugins.PEDESTAL_CRAFTING, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
	}
	
}