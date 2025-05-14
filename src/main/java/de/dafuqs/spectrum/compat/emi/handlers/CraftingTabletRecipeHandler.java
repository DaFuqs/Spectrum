package de.dafuqs.spectrum.compat.emi.handlers;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.inventories.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.recipe.handler.*;
import net.minecraft.world.inventory.*;

import java.util.*;

public class CraftingTabletRecipeHandler implements StandardRecipeHandler<CraftingTabletScreenHandler> {
    @Override
    public List<Slot> getInputSources(CraftingTabletScreenHandler handler) {
        List<Slot> slots = new ArrayList<>();

        // crafting slots
        slots.addAll(handler.slots.subList(0, 8));

        // player inventory & hotbar
        slots.addAll(handler.slots.subList(15, 51));

        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(CraftingTabletScreenHandler handler) {
        return handler.slots.subList(0, 14);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        EmiRecipeCategory category = recipe.getCategory();
        return (category == SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING || category == VanillaEmiRecipeCategories.CRAFTING) && recipe.supportsRecipeTree();
    }
}
