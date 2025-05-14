package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FusionShrineDisplay extends GatedSpectrumDisplay {
	
	protected final float experience;
	protected final int craftingTime;
	protected final Optional<Component> description;
	
	public FusionShrineDisplay(@NotNull RecipeHolder<FusionShrineRecipe> recipe) {
		super(recipe, buildIngredients(recipe.value()), recipe.value().getResultItem(BasicDisplay.registryAccess()));
		this.experience = recipe.value().getExperience();
		this.craftingTime = recipe.value().getCraftingTime();
		this.description = recipe.value().getDescription();
	}
	
	private static List<EntryIngredient> buildIngredients(FusionShrineRecipe recipe) {
		List<EntryIngredient> inputs = REIHelper.toEntryIngredients(recipe.getIngredientStacks());
		inputs.add(0, REIHelper.ofFluidIngredient(recipe.getFluid()));
		return inputs;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.FUSION_SHRINE;
	}
	
	@Override
    public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, FusionShrineRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
	public Optional<Component> getDescription() {
		return this.description;
	}
	
}
