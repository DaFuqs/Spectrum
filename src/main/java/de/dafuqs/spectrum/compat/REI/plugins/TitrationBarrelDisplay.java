package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.REIHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TitrationBarrelDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected boolean secret;
	protected final List<EntryIngredient> ingredients;
	protected final EntryIngredient tappingIngredient;
	protected final EntryIngredient output;
	protected final int minFermentationTimeHours;
	protected final Identifier requiredAdvancementIdentifier;
	protected final TitrationBarrelRecipe.FermentationData fermentationData;
	
	public TitrationBarrelDisplay(@NotNull ITitrationBarrelRecipe recipe) {
		this.secret = recipe.isSecret();
		this.ingredients = recipe.getIngredientStacks().stream().map(REIHelper::ofIngredientStack).collect(Collectors.toCollection(ArrayList::new));
		this.output = EntryIngredients.of(recipe.getOutput());
		if(recipe.getTappingItem() == Items.AIR) {
			this.tappingIngredient = EntryIngredient.empty();
		} else {
			this.tappingIngredient = EntryIngredients.of(recipe.getTappingItem().getDefaultStack());
		}

		
		this.minFermentationTimeHours = recipe.getMinFermentationTimeHours();
		this.fermentationData = recipe.getFermentationData();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return ingredients;
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		if (this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return Collections.singletonList(output);
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.TITRATION_BARREL;
	}
	
	@Override
	public boolean isUnlocked() {
		PlayerEntity player = MinecraftClient.getInstance().player;
		return AdvancementHelper.hasAdvancement(player, TitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER) && AdvancementHelper.hasAdvancement(player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public int getWidth() {
		return 3;
	}
	
	@Override
	public int getHeight() {
		return 3;
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}