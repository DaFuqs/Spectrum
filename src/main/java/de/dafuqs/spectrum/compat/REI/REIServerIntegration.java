package de.dafuqs.spectrum.compat.REI;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;

public class REIServerIntegration implements REIServerPlugin {
    
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        //TODO
        REIServerPlugin.super.registerDisplaySerializer(registry);
        //registry.register(PedestalCraftingCategory.ID, PedestalCraftingRecipeDisplay.serializer());
    }
    
    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        //registry.register(BuiltinPlugin.CRAFTING, PedestalScreenHandler.class);
        //registry.register(PedestalCraftingCategory.ID, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(PedestalGridMenuInfo::new));
    }
    
}