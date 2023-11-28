package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.client.*;
import net.minecraft.util.*;

public abstract class FluidConvertingDisplay extends GatedSpectrumDisplay {
	
	public FluidConvertingDisplay(FluidConvertingRecipe recipe) {
		super(recipe, recipe.getIngredients().get(0), recipe.getOutput(BasicDisplay.registryAccess()));
	}
	
	public final EntryIngredient getIn() {
		return getInputEntries().get(0);
	}
	
	public final EntryIngredient getOut() {
		return getOutputEntries().get(0);
	}
	
	@Override
    public boolean isUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, getUnlockIdentifier()) && super.isUnlocked();
	}
	
	public abstract Identifier getUnlockIdentifier();
	
}
