package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CinderhearthDisplay extends GatedSpectrumDisplay {
	
	protected final float experience;
	protected final int craftingTime;
	protected final List<Pair<ItemStack, Float>> outputsWithChance;
	
	public CinderhearthDisplay(@NotNull CinderhearthRecipe recipe) {
		super(recipe, Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredients().get(0))), List.of(EntryIngredients.ofItemStacks(recipe.getPossibleOutputs())));
		this.outputsWithChance = recipe.getOutputsWithChance();
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.CINDERHEARTH;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, CinderhearthRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}