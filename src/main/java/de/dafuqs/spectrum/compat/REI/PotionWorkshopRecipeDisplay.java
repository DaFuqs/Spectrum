package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopRecipe;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PotionWorkshopRecipeDisplay extends BasicDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {

	private final PotionWorkshopRecipe potionWorkshopRecipe;
	protected final List<EntryIngredient> craftingInputs;
	protected final EntryIngredient output;
	protected final int craftingTime;
	
	/**
	 * When using the REI recipe functionality
	 * @param recipe The recipe
	 */
	public PotionWorkshopRecipeDisplay(PotionWorkshopRecipe recipe) {
		super(recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new)), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		
		this.potionWorkshopRecipe = recipe;
		this.craftingInputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));
		this.output = EntryIngredients.of(recipe.getOutput());
		this.craftingTime = recipe.getCraftingTime();
	}
	
	/**
	 * When using Shift click on the plus button in the REI gui to autofill crafting grids
	 */
	public PotionWorkshopRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, PotionWorkshopRecipe recipe) {
		super(inputs, outputs);
		
		this.craftingInputs = inputs;
		this.output = outputs.get(0);
		this.potionWorkshopRecipe = recipe;
		this.craftingTime = recipe.getCraftingTime();
	}
	
	public List<EntryIngredient> getOrganisedInputEntries() {
		List<EntryIngredient> list = new ArrayList<>();
		List<EntryIngredient> inputs = getInputEntries();
		
		// crafting ingredients
		for (EntryIngredient input : inputs) {
			list.add(EntryIngredient.of(input));
		}
		
		return list;
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
			return outputs;
		} else {
			return new ArrayList<>();
		}
	}
	
	public boolean isUnlocked() {
		if(this.potionWorkshopRecipe == null) {
			return true;
		} else {
			return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER || isUnlockedClient();
		}
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isUnlockedClient() {
		return this.potionWorkshopRecipe.canPlayerCraft(MinecraftClient.getInstance().player);
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