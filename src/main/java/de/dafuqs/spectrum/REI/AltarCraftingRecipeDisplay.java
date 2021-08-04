package de.dafuqs.spectrum.REI;

import de.dafuqs.spectrum.enums.SpectrumColor;
import de.dafuqs.spectrum.progression.SpectrumClientAdvancements;
import de.dafuqs.spectrum.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AltarCraftingRecipeDisplay<R extends AltarCraftingRecipe> implements Display {

	private final List<Identifier> requiredAdvancementIdentifiers;
	protected final List<EntryIngredient> craftingInputs;
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;

	protected final int height;
	protected final int width;

	public AltarCraftingRecipeDisplay(AltarCraftingRecipe recipe) {
		this.craftingInputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));

		// since some recipes use less than 9 ingredients it will be serialized with less than 9 length.
		// => fill up to 9 so everything is in order
		/*while(craftingInputs.size() < 9) {
			craftingInputs.add(EntryIngredient.empty());
		}*/

		this.requiredAdvancementIdentifiers = recipe.getRequiredAdvancementIdentifiers();

		HashMap<SpectrumColor, Integer> spectrumInputs = recipe.getSpectrumInputs();
		addSpectrumCraftingInput(spectrumInputs, SpectrumColor.CYAN, SpectrumItems.TOPAZ_POWDER);
		addSpectrumCraftingInput(spectrumInputs, SpectrumColor.MAGENTA, SpectrumItems.AMETHYST_POWDER);
		addSpectrumCraftingInput(spectrumInputs, SpectrumColor.YELLOW, SpectrumItems.CITRINE_POWDER);
		addSpectrumCraftingInput(spectrumInputs, SpectrumColor.BLACK, SpectrumItems.ONYX_POWDER);
		addSpectrumCraftingInput(spectrumInputs, SpectrumColor.WHITE, SpectrumItems.MOONSTONE_POWDER);

		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.height = recipe.getHeight();
		this.width = recipe.getWidth();

	}

	private void addSpectrumCraftingInput(HashMap<SpectrumColor, Integer> spectrumInputs, SpectrumColor spectrumColor, Item item) {
		if(spectrumInputs.containsKey(spectrumColor)) {
			int amount = spectrumInputs.get(spectrumColor);
			if(amount > 0) {
				this.craftingInputs.add(EntryIngredients.of(new ItemStack(item, amount)));
			} else {
				this.craftingInputs.add(EntryIngredient.empty());
			}
		} else {
			this.craftingInputs.add(EntryIngredient.empty());
		}
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		if(this.isUnlocked()) {
			return craftingInputs;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		if(this.isUnlocked()) {
			return Collections.singletonList(output);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AltarCraftingCategory.ID;
	}

	public boolean isUnlocked() {
		for(Identifier advancementIdentifier : this.requiredAdvancementIdentifiers) {
			if(!SpectrumClientAdvancements.hasDone(advancementIdentifier)) {
				return false;
			}
		}
		return true;
	}


}