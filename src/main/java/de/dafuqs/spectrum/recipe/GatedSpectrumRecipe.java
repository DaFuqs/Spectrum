package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public abstract class GatedSpectrumRecipe<C extends RecipeInput> implements GatedRecipe<C> {
	
	public final String group;
	public final boolean secret;
	public final Optional<ResourceLocation> requiredAdvancementIdentifier;
	
	protected GatedSpectrumRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier) {
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
	
	/**
	 * The advancement the player has to have for the recipe be craftable
	 *
	 * @return The advancement identifier. A null value means the player is always able to craft this recipe
	 */
	@Override
	public Optional<ResourceLocation> getRequiredAdvancementIdentifier() {
		return this.requiredAdvancementIdentifier;
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return null;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	protected static ItemStack getDefaultStackWithCount(Item item, int count) {
		ItemStack stack = item.getDefaultInstance();
		stack.setCount(count);
		return stack;
	}
	
}
