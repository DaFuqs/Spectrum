package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.inventories.CraftingTabletScreenHandler;
import de.dafuqs.spectrum.inventories.PedestalScreenHandler;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.RecipeBookGridMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.DefaultPlugin;

public class REIServerIntegration implements REIServerPlugin {
    
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        //TODO
        //REIServerPlugin.super.registerDisplaySerializer(registry);
        //registry.register(PedestalCraftingCategory.ID, PedestalCraftingRecipeDisplay.serializer());
    }
    
    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(BuiltinPlugin.CRAFTING, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
        registry.register(BuiltinPlugin.CRAFTING, CraftingTabletScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
        //registry.register(PedestalCraftingCategory.ID, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(PedestalGridMenuInfo::new));
    }
    
}