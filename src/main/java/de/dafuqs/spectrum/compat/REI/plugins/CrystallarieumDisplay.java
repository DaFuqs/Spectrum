package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumCatalyst;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrystallarieumDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	protected boolean secret;
	protected final List<EntryIngredient> growthStages;
	protected final List<CrystallarieumCatalyst> catalysts;
	protected final InkColor inkColor;
	protected final boolean growsWithoutCatalyst;
	protected final Identifier requiredAdvancementIdentifier;
	protected final int secondsPerStage;
	
	public CrystallarieumDisplay(@NotNull CrystallarieumRecipe recipe) {
		super(Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredientStack())), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		
		this.secret = recipe.isSecret();
		this.growthStages = new ArrayList<>();
		for(BlockState state : recipe.getGrowthStages()) {
			growthStages.add(EntryIngredients.of(state.getBlock().asItem()));
		}
		this.catalysts = recipe.getCatalysts();
		this.inkColor = recipe.getInkColor();
		this.growsWithoutCatalyst = recipe.growsWithoutCatalyst();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		this.secondsPerStage = recipe.getSecondsPerGrowthStage();
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return super.getInputEntries();
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		if (this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return super.getOutputEntries();
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.CRYSTALLARIEUM;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}