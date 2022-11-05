package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.REIHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.*;

public class PedestalCraftingDisplay extends BasicDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected boolean secret;
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;
	
	protected final int width;
	protected final int height;
	
	protected final Identifier requiredAdvancementIdentifier;
	protected final PedestalRecipeTier pedestalRecipeTier;
	
	/**
	 * When using the REI recipe functionality
	 * @param recipe The recipe
	 */
	public PedestalCraftingDisplay(PedestalCraftingRecipe recipe) {
		super(mapIngredients(recipe), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		
		this.secret = recipe.isSecret();
		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		
		this.width = recipe.getWidth();
		this.height = recipe.getHeight();
		
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		this.pedestalRecipeTier = recipe.getTier();
	}
	
	private static List<EntryIngredient> mapIngredients(PedestalCraftingRecipe recipe) {
		int shownGemstoneSlotCount = recipe.getTier() == PedestalRecipeTier.COMPLEX ? 5 : recipe.getTier() == PedestalRecipeTier.ADVANCED ? 4 : 3;
		
		List<EntryIngredient> list = new ArrayList<>(9 + shownGemstoneSlotCount);
		for (int i = 0; i < 9 + shownGemstoneSlotCount; i++) {
			list.add(EntryIngredient.empty());
		}
		for (int i = 0; i < recipe.getIngredientStacks().size(); i++) {
			list.set(PedestalCraftingDisplaySerializer.getSlotWithSize(recipe.getWidth(), i), REIHelper.ofIngredientStack(recipe.getIngredientStacks().get(i)));
		}
		
		HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs = recipe.getGemstonePowderInputs();
		int firstGemstoneSlotId = 3*3;

		int cyan = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.CYAN, 0);
		if(cyan > 0) {
			list.set(firstGemstoneSlotId, EntryIngredients.of(SpectrumItems.TOPAZ_POWDER, cyan));
		}
		int magenta = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.MAGENTA, 0);
		if(magenta > 0) {
			list.set(firstGemstoneSlotId+1, EntryIngredients.of(SpectrumItems.AMETHYST_POWDER, magenta));
		}
		int yellow = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.YELLOW, 0);
		if(yellow > 0) {
			list.set(firstGemstoneSlotId+2, EntryIngredients.of(SpectrumItems.CITRINE_POWDER, yellow));
		}
		if(shownGemstoneSlotCount >= 4) {
			int black = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.BLACK, 0);
			if(black > 0) {
				list.set(firstGemstoneSlotId+3, EntryIngredients.of(SpectrumItems.ONYX_POWDER, black));
			}
			if(shownGemstoneSlotCount == 5) {
				int white = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.WHITE, 0);
				if(white > 0) {
					list.set(firstGemstoneSlotId+4, EntryIngredients.of(SpectrumItems.MOONSTONE_POWDER, white));
				}
			}
		}
		
		return list;
	}
	
	/**
	 * When using Shift click on the plus button in the REI gui to autofill crafting grids & recipe favourites
	 */
	public PedestalCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int width, int height, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, String recipeTier) {
		super(inputs, outputs);
		
		this.output = outputs.get(0);
		this.experience = experience;
		this.craftingTime = craftingTime;
		
		this.width = width;
		this.height = height;
		
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		this.pedestalRecipeTier = PedestalRecipeTier.valueOf(recipeTier.toUpperCase(Locale.ROOT));
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return inputs;
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
		return PedestalRecipeTier.hasUnlockedRequiredTier(MinecraftClient.getInstance().player, this.pedestalRecipeTier) && AdvancementHelper.hasAdvancementClient(this.requiredAdvancementIdentifier);
	}
	
	@Override
	public int getWidth() {
		return 3;
	}
	
	@Override
	public int getHeight() {
		return 3;
	}
	
	public PedestalRecipeTier getTier() {
		return this.pedestalRecipeTier;
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}