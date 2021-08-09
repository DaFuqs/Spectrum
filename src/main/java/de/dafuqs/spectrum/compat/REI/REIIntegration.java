package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;

@Environment(EnvType.CLIENT)
public class REIIntegration implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new AltarCraftingCategory<>());
        registry.add(new AnvilCrushingCategory<>());

        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(SpectrumBlocks.ALTAR));
        registry.addWorkstations(AltarCraftingCategory.ID, EntryStacks.of(SpectrumBlocks.ALTAR));
        registry.addWorkstations(AnvilCrushingCategory.ID, EntryStacks.of(Blocks.ANVIL));
        registry.addWorkstations(AnvilCrushingCategory.ID, EntryStacks.of(SpectrumBlocks.BEDROCK_ANVIL));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(AltarCraftingRecipe.class, AltarCraftingRecipeDisplay::new);
        registry.registerFiller(AnvilCrushingRecipe.class, AnvilCrushingRecipeDisplay::new);
    }

}
