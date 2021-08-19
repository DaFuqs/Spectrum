package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.progression.ClientAdvancements;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
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

public class PedestalCraftingRecipeDisplay<R extends PedestalCraftingRecipe> implements SimpleGridMenuDisplay {

	private final List<Identifier> requiredAdvancementIdentifiers;
	protected final List<EntryIngredient> craftingInputs;
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;

	protected final int height;
	protected final int width;

	public PedestalCraftingRecipeDisplay(PedestalCraftingRecipe recipe) {
		this.craftingInputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));

		this.requiredAdvancementIdentifiers = recipe.getRequiredAdvancementIdentifiers();

		HashMap<GemstoneColor, Integer> spectrumInputs = recipe.getGemstoneDustInputs();
		addSpectrumCraftingInput(spectrumInputs, GemstoneColor.CYAN, SpectrumItems.TOPAZ_POWDER);
		addSpectrumCraftingInput(spectrumInputs, GemstoneColor.MAGENTA, SpectrumItems.AMETHYST_POWDER);
		addSpectrumCraftingInput(spectrumInputs, GemstoneColor.YELLOW, SpectrumItems.CITRINE_POWDER);
		addSpectrumCraftingInput(spectrumInputs, GemstoneColor.BLACK, SpectrumItems.ONYX_POWDER);
		addSpectrumCraftingInput(spectrumInputs, GemstoneColor.WHITE, SpectrumItems.MOONSTONE_POWDER);

		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.height = recipe.getHeight();
		this.width = recipe.getWidth();

	}

	private void addSpectrumCraftingInput(HashMap<GemstoneColor, Integer> spectrumInputs, GemstoneColor gemstoneColor, Item item) {
		if(spectrumInputs.containsKey(gemstoneColor)) {
			int amount = spectrumInputs.get(gemstoneColor);
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
		return PedestalCraftingCategory.ID;
	}

	public boolean isUnlocked() {
		for(Identifier advancementIdentifier : this.requiredAdvancementIdentifiers) {
			if(!ClientAdvancements.hasDone(advancementIdentifier)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}

}