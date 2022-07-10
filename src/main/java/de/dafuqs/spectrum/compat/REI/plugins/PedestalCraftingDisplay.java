package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PedestalCraftingDisplay extends BasicDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected final List<EntryIngredient> craftingInputs;
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;
	protected final PedestalRecipeTier pedestalRecipeTier;
	private final PedestalCraftingRecipe pedestalCraftingRecipe;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PedestalCraftingDisplay(PedestalCraftingRecipe recipe) {
		super(recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new)), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		
		this.pedestalCraftingRecipe = recipe;
		this.craftingInputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));
		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.pedestalRecipeTier = recipe.getTier();
		
		HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs = recipe.getGemstonePowderInputs();
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.CYAN, SpectrumItems.TOPAZ_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.MAGENTA, SpectrumItems.AMETHYST_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.YELLOW, SpectrumItems.CITRINE_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.BLACK, SpectrumItems.ONYX_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.WHITE, SpectrumItems.MOONSTONE_POWDER);
	}
	
	/**
	 * When using Shift click on the plus button in the REI gui to autofill crafting grids
	 */
	public PedestalCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Recipe recipe) {
		super(inputs, outputs);
		
		this.craftingInputs = inputs;
		this.output = outputs.get(0);
		if (recipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe) {
			this.pedestalCraftingRecipe = pedestalCraftingRecipe;
			this.experience = pedestalCraftingRecipe.getExperience();
			this.craftingTime = pedestalCraftingRecipe.getCraftingTime();
			this.pedestalRecipeTier = pedestalCraftingRecipe.getTier();
		} else {
			this.pedestalCraftingRecipe = null;
			this.experience = 0;
			this.craftingTime = 0;
			this.pedestalRecipeTier = PedestalRecipeTier.BASIC;
		}
	}
	
	public static Serializer<PedestalCraftingDisplay> serializer() {
		return PedestalCraftingDisplay.Serializer.ofSimple(PedestalCraftingDisplay::simple)
				.inputProvider(PedestalCraftingDisplay::getOrganisedInputEntries);
	}
	
	private static @NotNull PedestalCraftingDisplay simple(List<EntryIngredient> inputs, List<EntryIngredient> outputs, @NotNull Optional<Identifier> identifier) {
		Recipe<?> optionalRecipe = identifier.flatMap(resourceLocation -> RecipeManagerContext.getInstance().getRecipeManager().get(resourceLocation))
				.orElse(null);
		return new PedestalCraftingDisplay(inputs, outputs, optionalRecipe);
	}
	
	public static int getSlotWithSize(@NotNull PedestalCraftingDisplay display, int index) {
		return getSlotWithSize(display.getWidth(), index);
	}
	
	public static int getSlotWithSize(int recipeWidth, int index) {
		int x = index % recipeWidth;
		int y = (index - x) / recipeWidth;
		return 3 * y + x;
	}
	
	public List<EntryIngredient> getOrganisedInputEntries() {
		List<EntryIngredient> list = new ArrayList<>();
		List<EntryIngredient> inputs = getInputEntries();
		int gemstonePowderStartIndex = inputs.size() - 5;
		
		// crafting ingredients
		for (int i = 0; i < 9; i++) {
			list.add(EntryIngredient.empty());
		}
		for (int j = 0; j < gemstonePowderStartIndex; j++) {
			list.set(getSlotWithSize(this, j), inputs.get(j));
		}
		
		// gemstone powder ingredients
		for (int k = gemstonePowderStartIndex; k < inputs.size(); k++) {
			list.add(inputs.get(k));
		}
		return list;
	}
	
	private void addGemstonePowderCraftingInput(@NotNull HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs, BuiltinGemstoneColor gemstoneColor, Item item) {
		if (gemstonePowderInputs.containsKey(gemstoneColor)) {
			int amount = gemstonePowderInputs.get(gemstoneColor);
			if (amount > 0) {
				this.craftingInputs.add(EntryIngredients.of(item, amount));
			} else {
				this.craftingInputs.add(EntryIngredient.empty());
			}
		} else {
			this.craftingInputs.add(EntryIngredient.empty());
		}
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return craftingInputs;
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		if (this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return outputs;
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.PEDESTAL_CRAFTING;
	}
	
	public boolean isUnlocked() {
		if (this.pedestalCraftingRecipe == null) {
			return true;
		} else {
			return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER || isUnlockedClient();
		}
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isUnlockedClient() {
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