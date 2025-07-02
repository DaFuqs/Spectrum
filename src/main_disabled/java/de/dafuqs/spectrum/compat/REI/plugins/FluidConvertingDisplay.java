package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.client.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

public abstract class FluidConvertingDisplay extends GatedSpectrumDisplay {
	
	public <T extends FluidConvertingRecipe> FluidConvertingDisplay(RecipeHolder<T> recipe) {
		super(recipe, recipe.value().getIngredients().getFirst(), recipe.value().getResultItem(BasicDisplay.registryAccess()));
	}
	
	public final EntryIngredient getIn() {
		return getInputEntries().getFirst();
	}
	
	public final EntryIngredient getOut() {
		return getOutputEntries().getFirst();
	}
	
	@Override
    public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, getUnlockIdentifier()) && super.isUnlocked();
	}
	
	public abstract ResourceLocation getUnlockIdentifier();
	
}
