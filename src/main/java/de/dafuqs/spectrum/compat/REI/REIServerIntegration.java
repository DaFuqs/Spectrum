package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.compat.REI.info.PedestalGridMenuInfo;
import de.dafuqs.spectrum.inventories.PedestalScreenHandler;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

import java.util.stream.IntStream;

public class REIServerIntegration implements REIServerPlugin {
    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(BuiltinPlugin.CRAFTING, PedestalScreenHandler.class,
        registry.register(PedestalCraftingCategory.ID, PedestalScreenHandler.class, SimpleMenuInfoProvider.of(PedestalGridMenuInfo::new)));
    }
    
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        //TODO
        REIServerPlugin.super.registerDisplaySerializer(registry);
        //registry.register(PedestalCraftingCategory.ID, PedestalCraftingRecipeDisplay.serializer());
    }
}