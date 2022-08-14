package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
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
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.math.random.Random;
import java.util.stream.Collectors;
import java.util.*;

public class PedestalCraftingDisplay extends BasicDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;
	
	protected final Identifier requiredAdvancementIdentifier;
	protected final PedestalRecipeTier pedestalRecipeTier;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PedestalCraftingDisplay(PedestalCraftingRecipe recipe) {
		super(mapIngredients(recipe), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		
		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		this.pedestalRecipeTier = recipe.getTier();
		
		HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs = recipe.getGemstonePowderInputs();
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.CYAN, SpectrumItems.TOPAZ_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.MAGENTA, SpectrumItems.AMETHYST_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.YELLOW, SpectrumItems.CITRINE_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.BLACK, SpectrumItems.ONYX_POWDER);
		addGemstonePowderCraftingInput(gemstonePowderInputs, BuiltinGemstoneColor.WHITE, SpectrumItems.MOONSTONE_POWDER);
	}
	
	private static List<EntryIngredient> mapIngredients(PedestalCraftingRecipe recipe) {
		int shownGemstoneSlotCount = recipe.getTier() == PedestalRecipeTier.COMPLEX ? 5 : recipe.getTier() == PedestalRecipeTier.ADVANCED ? 4 : 3;
		List<EntryIngredient> list = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));
		while(list.size() < 9 + shownGemstoneSlotCount) {
			list.add(EntryIngredient.empty());
		}
		return list;
	}
	
	/**
	 * When using Shift click on the plus button in the REI gui to autofill crafting grids & recipe favourites
	 */
	public PedestalCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, String recipeTier) {
		super(inputs, outputs);
		
		this.output = outputs.get(0);
		this.experience = experience;
		this.craftingTime = craftingTime;
		
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		this.pedestalRecipeTier = PedestalRecipeTier.valueOf(recipeTier.toUpperCase(Locale.ROOT));
	}
	
	public static Serializer<PedestalCraftingDisplay> serializer() {
		return BasicDisplay.Serializer.of((input, output, location, tag) -> {
			float experience = tag.getFloat("Experience");
			int craftingTime = tag.getInt("CraftingTime");
			String recipeTier = tag.getString("RecipeTier");
			Identifier requiredAdvancementIdentifier = Identifier.tryParse(tag.getString("Advancement"));
			return PedestalCraftingDisplay.simple(input, output, experience, craftingTime, requiredAdvancementIdentifier, recipeTier);
		}, (display, tag) -> {
			tag.putFloat("Experience", display.experience);
			tag.putInt("CraftingTime", display.craftingTime);
			tag.putString("RecipeTier", display.pedestalRecipeTier.toString());
			tag.putString("Advancement", display.requiredAdvancementIdentifier.toString());
			tag.putInt("Width", display.getWidth());
		});
	}
	
	private static @NotNull PedestalCraftingDisplay simple(List<EntryIngredient> inputs, List<EntryIngredient> outputs, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, String recipeTier) {
		return new PedestalCraftingDisplay(inputs, outputs, experience, craftingTime, requiredAdvancementIdentifier, recipeTier);
	}
	
	private void addGemstonePowderCraftingInput(@NotNull HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs, BuiltinGemstoneColor gemstoneColor, Item item) {
		if (gemstonePowderInputs.containsKey(gemstoneColor)) {
			int amount = gemstonePowderInputs.get(gemstoneColor);
			if (amount > 0) {
				this.inputs.add(EntryIngredients.of(item, amount));
			} else {
				this.inputs.add(EntryIngredient.empty());
			}
		} else {
			this.inputs.add(EntryIngredient.empty());
		}
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
		return PedestalRecipeTier.hasUnlockedRequiredTier(MinecraftClient.getInstance().player, this.pedestalRecipeTier) && AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
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
	
}