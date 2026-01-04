package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public abstract class GatedItemInformationDisplay extends GatedSpectrumDisplay {
	
	protected final Item item;
	protected final Component description;
	
	public GatedItemInformationDisplay(RecipeHolder<? extends DescriptiveGatedRecipe<?>> recipe) {
		super(recipe, Collections.singletonList(EntryIngredients.of(recipe.value().getItem())), Collections.emptyList());
		this.item = recipe.value().getItem();
		this.description = recipe.value().getDescription();
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public Component getDescription() {
		return this.description;
	}
	
}