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
	
	protected final ItemStack result;
	protected final String group;
	protected final boolean secret;
	protected final Optional<ResourceLocation> requiredAdvancementIdentifier;

	public GatedCraftingRecipe(String group, CraftingBookCategory category, ItemStack result, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier) {
		super(category);
		this.result = result;
		this.group = group;
		this.secret = secret;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public boolean isSecret() {
		return this.secret;
	}

	@Override
	public Optional<ResourceLocation> getRequiredAdvancementIdentifier() {
		return this.requiredAdvancementIdentifier;
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
	
	@Override
	public String getRecipeTypeShortID() {
		return "gated_crafting";
	}
	
}