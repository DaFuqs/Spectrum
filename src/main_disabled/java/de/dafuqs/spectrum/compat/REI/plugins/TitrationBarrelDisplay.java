package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TitrationBarrelDisplay extends GatedSpectrumDisplay {
	
	protected final EntryIngredient tappingIngredient;
	protected final int minFermentationTimeHours;
	protected final FermentationData fermentationData;
	
	public TitrationBarrelDisplay(@NotNull RecipeHolder<ITitrationBarrelRecipe> recipe) {
		super(recipe, buildInputs(recipe.value()), List.of(buildOutputs(recipe.value())));
		if (recipe.value().getTappingItem() == Items.AIR) {
			this.tappingIngredient = EntryIngredient.empty();
		} else {
			this.tappingIngredient = EntryIngredients.of(recipe.value().getTappingItem().getDefaultInstance());
		}
		this.minFermentationTimeHours = recipe.value().getMinFermentationTimeHours();
		this.fermentationData = recipe.value().getFermentationData();
	}
	
	private static EntryIngredient buildOutputs(ITitrationBarrelRecipe recipe) {
		if (recipe instanceof TitrationBarrelRecipe titrationBarrelRecipe && titrationBarrelRecipe.getFermentationData() != null) {
			return EntryIngredients.ofItemStacks(titrationBarrelRecipe.getOutputVariations(TitrationBarrelRecipe.FERMENTATION_DURATION_DISPLAY_TIME_MULTIPLIERS));
		} else {
			return EntryIngredients.of(recipe.getResultItem(BasicDisplay.registryAccess()));
		}
	}
	
	public static List<EntryIngredient> buildInputs(ITitrationBarrelRecipe recipe) {
		List<EntryIngredient> inputs = REIHelper.toEntryIngredients(recipe.getIngredientStacks());
		if (recipe.getFluidInput() != FluidIngredient.EMPTY) {
			inputs.add(FluidIngredientREI.into(recipe.getFluidInput()));
		}
		return inputs;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.TITRATION_BARREL;
	}
	
	@Override
    public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, TitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER) && super.isUnlocked();
	}
	
}
