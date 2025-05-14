package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CinderhearthDisplay extends GatedSpectrumDisplay {
	
	protected final float experience;
	protected final int craftingTime;
	protected final List<Tuple<ItemStack, Float>> outputsWithChance;
	
	public CinderhearthDisplay(@NotNull RecipeHolder<CinderhearthRecipe> recipe) {
		super(recipe, REIHelper.toEntryIngredients(recipe.value().getIngredientStacks()), List.of(EntryIngredients.ofItemStacks(recipe.value().getPossibleOutputs())));
		this.outputsWithChance = recipe.value().getResultsWithChance();
		this.experience = recipe.value().getExperience();
		this.craftingTime = recipe.value().getCraftingTime();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.CINDERHEARTH;
	}
	
	@Override
	public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, CinderhearthRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}
