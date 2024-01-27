package de.dafuqs.spectrum.compat.emi.handlers;

import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipeCategories;
import de.dafuqs.spectrum.inventories.PotionWorkshopScreenHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class PotionWorkshopRecipeHandler implements StandardRecipeHandler<PotionWorkshopScreenHandler> {
    @Override
    public List<Slot> getInputSources(PotionWorkshopScreenHandler handler) {
        List<Slot> slots = new ArrayList<>();
        slots.addAll(handler.slots.subList(0, 9));
        slots.addAll(handler.slots.subList(21, 57));
        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(PotionWorkshopScreenHandler handler) {
        return handler.slots.subList(0, 9);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        EmiRecipeCategory category = recipe.getCategory();
        return (category == SpectrumEmiRecipeCategories.POTION_WORKSHOP_BREWING || category == SpectrumEmiRecipeCategories.POTION_WORKSHOP_CRAFTING) && recipe.supportsRecipeTree();
    }
}
