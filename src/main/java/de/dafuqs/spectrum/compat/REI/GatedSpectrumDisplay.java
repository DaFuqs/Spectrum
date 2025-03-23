package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.recipe.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class GatedSpectrumDisplay extends BasicDisplay implements GatedRecipeDisplay {

	private final Identifier requiredAdvancementIdentifier;
	private final boolean secret;
	private final @Nullable Text secretHintText;
	
	// 1 input => 1 output
	public GatedSpectrumDisplay(GatedRecipe<? extends Inventory> recipe, Ingredient input, ItemStack output) {
		this(recipe, Collections.singletonList(EntryIngredients.ofIngredient(input)), Collections.singletonList(EntryIngredients.of(output)));
	}

	// n inputs => 1 output
	public GatedSpectrumDisplay(GatedRecipe<? extends Inventory> recipe, List<EntryIngredient> inputs, ItemStack output) {
		this(recipe, inputs, Collections.singletonList(EntryIngredients.of(output)));
	}

	// n inputs => m outputs
	public GatedSpectrumDisplay(GatedRecipe<? extends Inventory> recipe, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
		this.secret = recipe.isSecret();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		this.secretHintText = recipe.getSecretHintText();
	}

	@Override
	public boolean isUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, this.requiredAdvancementIdentifier);
	}

	@Override
	public boolean isSecret() {
		return this.secret;
	}
	
	public @Nullable Text getSecretHintText() {
		return this.secretHintText;
	}

}