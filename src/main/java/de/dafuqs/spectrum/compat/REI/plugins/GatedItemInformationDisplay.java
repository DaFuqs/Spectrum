package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.item.*;
import net.minecraft.text.*;

import java.util.*;

public abstract class GatedItemInformationDisplay extends GatedSpectrumDisplay {
	
	protected final Item item;
	protected final Text description;
	
	public GatedItemInformationDisplay(DescriptiveGatedRecipe recipe) {
		super(recipe, Collections.singletonList(EntryIngredients.of(recipe.getItem())), Collections.emptyList());
		this.item = recipe.getItem();
		this.description = recipe.getDescription();
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public Text getDescription() {
		return this.description;
	}
	
}