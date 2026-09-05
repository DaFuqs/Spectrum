package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.recipe.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.*;

import java.util.*;

public abstract class GatedSpectrumDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	private final @Nullable ResourceLocation requiredAdvancementIdentifier;
	private final @Nullable ResourceLocation revealSecretAdvancement;
	private final @Nullable Component secretHintText;
	
	// 1 input => 1 output
	public GatedSpectrumDisplay(RecipeHolder<? extends GatedRecipe<?>> recipe, Ingredient input, ItemStack output) {
		this(recipe, Collections.singletonList(EntryIngredients.ofIngredient(input)), Collections.singletonList(EntryIngredients.of(output)));
	}
	
	// n inputs => 1 output
	public GatedSpectrumDisplay(RecipeHolder<? extends GatedRecipe<?>> recipe, List<EntryIngredient> inputs, ItemStack output) {
		this(recipe, inputs, Collections.singletonList(EntryIngredients.of(output)));
	}
	
	// n inputs => m outputs
	public GatedSpectrumDisplay(RecipeHolder<? extends GatedRecipe<?>> recipe, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
		this.requiredAdvancementIdentifier = recipe.value().getRequiredAdvancement().orElse(null);
		this.revealSecretAdvancement = recipe.value().getRevealSecretAdvancement().orElse(null);
		this.secretHintText = recipe.value().getSecretHintText(recipe.id());
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(Minecraft.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean isSecret() {
		if(this.revealSecretAdvancement == null) {
			return false;
		}
		return !AdvancementHelper.hasAdvancement(Minecraft.getInstance().player, this.revealSecretAdvancement);
	}
	
	public @Nullable Component getSecretHintText() {
		return this.secretHintText;
	}
	
}
