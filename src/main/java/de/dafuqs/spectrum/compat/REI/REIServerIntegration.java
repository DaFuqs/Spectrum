package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.inventories.PedestalScreenHandler;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.display.SimpleDisplaySerializer;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import me.shedaniel.rei.impl.common.display.DisplaySerializerRegistryImpl;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.nbt.NbtCompound;

import java.util.stream.IntStream;

public class REIServerIntegration implements REIServerPlugin {
    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        
        registry.register(BuiltinPlugin.CRAFTING, PedestalScreenHandler.class, new SimpleGridMenuInfo<>() {
            
            @Override
            public int getCraftingResultSlotIndex(PedestalScreenHandler menu) {
                return -1;
            }
    
            @Override
            public int getCraftingWidth(PedestalScreenHandler menu) {
                return 3;
            }
    
            @Override
            public int getCraftingHeight(PedestalScreenHandler menu) {
                return 3;
            }
        });
    
        registry.register(PedestalCraftingCategory.ID, PedestalScreenHandler.class, new SimpleGridMenuInfo<>() {
    
            @Override
            public IntStream getInputStackSlotIds(MenuInfoContext<PedestalScreenHandler, ?, SimpleGridMenuDisplay> context) {
                return IntStream.range(0, 13);
            }
    
            @Override
            public int getCraftingResultSlotIndex(PedestalScreenHandler menu) {
                return -1;
            }
        
            @Override
            public int getCraftingWidth(PedestalScreenHandler menu) {
                return 3;
            }
        
            @Override
            public int getCraftingHeight(PedestalScreenHandler menu) {
                return 3;
            }
        });
        
    }
    
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        //TODO
        REIServerPlugin.super.registerDisplaySerializer(registry);
        
        registry.register(PedestalCraftingCategory.ID, PedestalCraftingRecipeDisplay.serializer());
    }
}