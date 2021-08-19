package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.inventories.PedestalScreen;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;

@Environment(EnvType.CLIENT)
public class REIIntegration implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PedestalCraftingCategory<>());
        registry.add(new AnvilCrushingCategory<>());

        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(SpectrumBlocks.PEDESTAL));
        registry.addWorkstations(PedestalCraftingCategory.ID, EntryStacks.of(SpectrumBlocks.PEDESTAL));
        registry.addWorkstations(AnvilCrushingCategory.ID, EntryStacks.of(Blocks.ANVIL), EntryStacks.of(SpectrumBlocks.BEDROCK_ANVIL));

        // Since anvil crushing is an in world recipe there is no gui to fill
        // therefore the plus button is obsolete
        registry.removePlusButton(AnvilCrushingCategory.ID);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(AnvilCrushingRecipe.class, AnvilCrushingRecipeDisplay::new);
        registry.registerRecipeFiller(PedestalCraftingRecipe.class, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingRecipeDisplay::new);
    }

    /**
     * Where in the screens gui the player has to click
     * to get to the recipe overview
     */
    @Override
    public void registerScreens(ScreenRegistry registry) {
        // Since the pedestal can craft both vanilla and pedestal recipes
        // we have to split the "arrow" part of the gui into two parts
        registry.registerContainerClickArea(new Rectangle(89, 37, 11, 15), PedestalScreen.class, BuiltinPlugin.CRAFTING);
        registry.registerContainerClickArea(new Rectangle(100, 37, 11, 15), PedestalScreen.class, PedestalCraftingCategory.ID);
    }

}
