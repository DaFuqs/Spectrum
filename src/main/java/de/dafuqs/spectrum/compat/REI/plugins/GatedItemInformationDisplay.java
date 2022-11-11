package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.recipe.DescriptiveGatedRecipe;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collections;

public abstract class GatedItemInformationDisplay extends GatedSpectrumDisplay {
	
	protected final Item item;
	protected final TranslatableText description;
	
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