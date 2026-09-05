package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

// We cannot extend ShapedRecipe / ShapelessRecipe, since EMI would force-register its own recipe handler for it in dev.emi.emi.VanillaPlugin. Big sad.
// Our Fallback: EMI hardcodes CustomRecipe to not register its default recipe display.
public abstract class GatedCraftingRecipe extends CustomRecipe implements GatedRecipe<CraftingInput> {
	
	public final String group;
	public final Optional<ResourceLocation> requiredAdvancement;
	public final Optional<ResourceLocation> revealSecretAdvancement;
	protected final List<ItemStack> additionalResults; // these aren't actual results, but recipe managers will treat it as such, showing this recipe as a way to get them. Use for drops of the growth blocks, for example
	
	protected final ItemStack result;

	public GatedCraftingRecipe(String group, Optional<ResourceLocation> requiredAdvancement, Optional<ResourceLocation> revealSecretAdvancement, List<ItemStack> additionalResults, CraftingBookCategory category, ItemStack result) {
		super(category);
		this.group = group;
		this.requiredAdvancement = requiredAdvancement;
		this.revealSecretAdvancement = revealSecretAdvancement;
		this.additionalResults = additionalResults;
		this.result = result;
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public Optional<ResourceLocation> getRevealSecretAdvancement() {
		return this.revealSecretAdvancement;
	}

	@Override
	public Optional<ResourceLocation> getRequiredAdvancement() {
		return this.requiredAdvancement;
	}

	@Override
	public @Nullable ResourceLocation getRecipeTypeUnlockIdentifier() {
		return null;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		return this.getResultItem(registries).copy();
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return this.result;
	}
	
	protected ItemStack getResult() {
		return this.result;
	}
	
	public List<ItemStack> getAdditionalResults() {
		return additionalResults;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "gated_crafting";
	}
	
}