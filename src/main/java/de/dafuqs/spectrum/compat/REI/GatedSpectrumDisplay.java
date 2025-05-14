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
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class GatedSpectrumDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	private final ResourceLocation requiredAdvancementIdentifier;
	private final boolean secret;
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
		this.secret = recipe.value().isSecret();
		this.requiredAdvancementIdentifier = recipe.value().getRequiredAdvancementIdentifier().orElse(null);
		// FIXME
		//this.secretHintText = recipe.getSecretHintText(id);
		this.secretHintText = null;
	}
	
	@Override
	public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean isSecret() {
		return this.secret;
	}
	
	public @Nullable Component getSecretHintText() {
		return this.secretHintText;
	}
	
}
