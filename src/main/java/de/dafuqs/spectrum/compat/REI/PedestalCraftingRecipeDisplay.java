package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PedestalCraftingRecipeDisplay<R extends PedestalCraftingRecipe> extends BasicDisplay implements SimpleGridMenuDisplay {

	private final PedestalCraftingRecipe pedestalCraftingRecipe;
	protected final List<EntryIngredient> craftingInputs;
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;
	protected final PedestalRecipeTier pedestalRecipeTier;

	public PedestalCraftingRecipeDisplay(PedestalCraftingRecipe recipe) {
		super(recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new)), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		this.pedestalCraftingRecipe = recipe;
		this.craftingInputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));

		HashMap<GemstoneColor, Integer> spectrumInputs = recipe.getGemstonePowderInputs();
		addGemstonePowderCraftingInput(spectrumInputs, GemstoneColor.CYAN, SpectrumItems.TOPAZ_POWDER);
		addGemstonePowderCraftingInput(spectrumInputs, GemstoneColor.MAGENTA, SpectrumItems.AMETHYST_POWDER);
		addGemstonePowderCraftingInput(spectrumInputs, GemstoneColor.YELLOW, SpectrumItems.CITRINE_POWDER);
		addGemstonePowderCraftingInput(spectrumInputs, GemstoneColor.BLACK, SpectrumItems.ONYX_POWDER);
		addGemstonePowderCraftingInput(spectrumInputs, GemstoneColor.WHITE, SpectrumItems.MOONSTONE_POWDER);

		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.pedestalRecipeTier = recipe.getTier();
	}
	
	// TODO: I definitely need some help here
	/*public static BasicDisplay.Serializer<PedestalCraftingRecipeDisplay<?>> serializer() {
		//return BasicDisplay.Serializer.<PedestalCraftingRecipeDisplay<?>>ofSimple(DefaultCustomDisplay::simple).inputProvider(display -> display.getOrganisedInputEntries(3, 3));
		return BasicDisplay.Serializer.ofRecipeLess(PedestalCraftingRecipeDisplay::new);
	}*/
	
	private void addGemstonePowderCraftingInput(HashMap<GemstoneColor, Integer> gemstonePowderInputs, GemstoneColor gemstoneColor, Item item) {
		if(gemstonePowderInputs.containsKey(gemstoneColor)) {
			int amount = gemstonePowderInputs.get(gemstoneColor);
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
		return this.pedestalCraftingRecipe.canPlayerCraft(MinecraftClient.getInstance().player);
	}

	@Override
	public int getWidth() {
		return this.pedestalCraftingRecipe.getWidth();
	}

	@Override
	public int getHeight() {
		return this.pedestalCraftingRecipe.getHeight();
	}

	public PedestalRecipeTier getTier() {
		return this.pedestalRecipeTier;
	}


}