package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FusionShrineDisplay extends GatedSpectrumDisplay {
	
	protected final float experience;
	protected final int craftingTime;
	protected final Optional<Text> description;
	
	public FusionShrineDisplay(@NotNull FusionShrineRecipe recipe) {
		super(recipe, buildIngredients(recipe), recipe.getOutput(null));
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.description = recipe.getDescription();
	}
	
	private static List<EntryIngredient> buildIngredients(FusionShrineRecipe recipe) {
		List<EntryIngredient> inputs = REIHelper.toEntryIngredients(recipe.getIngredientStacks());
		inputs.add(0, EntryIngredients.of(recipe.getFluidInput()));
		return inputs;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.FUSION_SHRINE;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, FusionShrineRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
	public Optional<Text> getDescription() {
		return this.description;
	}
	
}