package de.dafuqs.spectrum.compat.emi.handlers;

import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipeCategories;
import de.dafuqs.spectrum.inventories.CinderhearthScreenHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class CinderhearthRecipeHandler implements StandardRecipeHandler<CinderhearthScreenHandler> {
    @Override
    public List<Slot> getInputSources(CinderhearthScreenHandler handler) {
        List<Slot> slots = new ArrayList<>();
        slots.add(handler.getSlot(2));
        slots.addAll(handler.slots.subList(11, 47));
        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(CinderhearthScreenHandler handler) {
        return List.of(handler.getSlot(2));
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        EmiRecipeCategory category = recipe.getCategory();
        return (category == SpectrumEmiRecipeCategories.CINDERHEARTH || category == VanillaEmiRecipeCategories.BLASTING) && recipe.supportsRecipeTree();
    }
}
