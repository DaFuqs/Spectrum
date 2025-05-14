package de.dafuqs.spectrum.compat.emi.handlers;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.inventories.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.recipe.handler.*;
import net.minecraft.world.inventory.*;

import java.util.*;

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
