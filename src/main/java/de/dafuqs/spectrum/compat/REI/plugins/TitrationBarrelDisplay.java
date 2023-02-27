package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TitrationBarrelDisplay extends GatedSpectrumDisplay {

	private static final List<Integer> FERMENTATION_DURATION_DISPLAY_TIME_MULTIPLIERS = new ArrayList<>() {{
		add(1);
		add(10);
		add(100);
	}};

	protected final EntryIngredient tappingIngredient;
	protected final int minFermentationTimeHours;
	protected final TitrationBarrelRecipe.FermentationData fermentationData;

	public TitrationBarrelDisplay(@NotNull ITitrationBarrelRecipe recipe) {
		super(recipe, buildInputs(recipe), List.of(buildOutputs(recipe)));
		if (recipe.getTappingItem() == Items.AIR) {
			this.tappingIngredient = EntryIngredient.empty();
		} else {
			this.tappingIngredient = EntryIngredients.of(recipe.getTappingItem().getDefaultStack());
		}
		this.minFermentationTimeHours = recipe.getMinFermentationTimeHours();
		this.fermentationData = recipe.getFermentationData();
	}

	private static EntryIngredient buildOutputs(ITitrationBarrelRecipe recipe) {
		if (recipe instanceof TitrationBarrelRecipe titrationBarrelRecipe && titrationBarrelRecipe.getFermentationData() != null) {
			return EntryIngredients.ofItemStacks(titrationBarrelRecipe.getOutputVariations(FERMENTATION_DURATION_DISPLAY_TIME_MULTIPLIERS));
		} else {
			return EntryIngredients.of(recipe.getOutput());
		}
	}

	public static List<EntryIngredient> buildInputs(ITitrationBarrelRecipe recipe) {
		List<EntryIngredient> inputs = REIHelper.toEntryIngredients(recipe.getIngredientStacks());
		if (recipe.getFluid() != Fluids.EMPTY) {
			inputs.add(EntryIngredients.of(recipe.getFluid().getBucketItem()));
		}
		return inputs;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.TITRATION_BARREL;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, TitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER) && super.isUnlocked();
	}
	
}