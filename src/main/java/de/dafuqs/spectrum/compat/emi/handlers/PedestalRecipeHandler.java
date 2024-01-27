package de.dafuqs.spectrum.compat.emi.handlers;

import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipeCategories;
import de.dafuqs.spectrum.inventories.PedestalScreenHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class PedestalRecipeHandler implements StandardRecipeHandler<PedestalScreenHandler> {
    @Override
    public List<Slot> getInputSources(PedestalScreenHandler handler) {
        List<Slot> slots = new ArrayList<>();

        // crafting & gemstone powder slots
        slots.addAll(handler.slots.subList(0, 14));

        // player inventory & hotbar
        slots.addAll(handler.slots.subList(16, 52));

        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(PedestalScreenHandler handler) {
        return handler.slots.subList(0, 14);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        EmiRecipeCategory category = recipe.getCategory();
        return (category == SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING || category == VanillaEmiRecipeCategories.CRAFTING) && recipe.supportsRecipeTree();
    }
}
