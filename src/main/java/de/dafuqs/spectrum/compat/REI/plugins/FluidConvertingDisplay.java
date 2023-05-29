package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.client.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

public abstract class FluidConvertingDisplay extends GatedSpectrumDisplay {

    public FluidConvertingDisplay(FluidConvertingRecipe recipe) {
        super(recipe, recipe.getIngredients().get(0), recipe.getOutput(DynamicRegistryManager.EMPTY));
    }

    public final EntryIngredient getIn() {
        return getInputEntries().get(0);
    }

    public final EntryIngredient getOut() {
        return getOutputEntries().get(0);
    }

    @Override
    public boolean isUnlocked() {
        return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, getUnlockIdentifier()) && super.isUnlocked();
    }

    public abstract Identifier getUnlockIdentifier();

}