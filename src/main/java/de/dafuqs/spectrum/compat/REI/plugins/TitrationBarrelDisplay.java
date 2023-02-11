package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.compat.REI.REIHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TitrationBarrelDisplay extends GatedSpectrumDisplay {
	
	protected final EntryIngredient tappingIngredient;
	protected final int minFermentationTimeHours;
	protected final TitrationBarrelRecipe.FermentationData fermentationData;
	
	public TitrationBarrelDisplay(@NotNull ITitrationBarrelRecipe recipe) {
		super(recipe, buildInputs(recipe), recipe.getOutput());
		if(recipe.getTappingItem() == Items.AIR) {
			this.tappingIngredient = EntryIngredient.empty();
		} else {
			this.tappingIngredient = EntryIngredients.of(recipe.getTappingItem().getDefaultStack());
		}
		this.minFermentationTimeHours = recipe.getMinFermentationTimeHours();
		this.fermentationData = recipe.getFermentationData();
	}
	
	public static List<EntryIngredient> buildInputs(ITitrationBarrelRecipe recipe) {
		List<EntryIngredient> inputs = REIHelper.toEntryIngredients(recipe.getIngredientStacks());
		if(recipe.getFluid() != Fluids.EMPTY) {
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