package de.dafuqs.spectrum.recipe;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public abstract class GatedSpectrumRecipe implements GatedRecipe {
	
	public final Identifier id;
	public final String group;
	public final boolean secret;
	public final Identifier requiredAdvancementIdentifier;
	
	protected GatedSpectrumRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.secret = secret;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}
	
	@Override
	public Identifier getId() {
		return this.id;
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
	@Nullable
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return this.requiredAdvancementIdentifier;
	}
	
	@Override
	public abstract Identifier getRecipeTypeUnlockIdentifier();
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, getRecipeTypeUnlockIdentifier())
				&& AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	public abstract String getRecipeTypeShortID();
	
	@Override
	public Text getSingleUnlockToastString() {
		return Text.translatable("spectrum.toast." + getRecipeTypeShortID() + "_recipe_unlocked.title");
	}
	
	@Override
	public Text getMultipleUnlockToastString() {
		return Text.translatable("spectrum.toast." + getRecipeTypeShortID() + "_recipes_unlocked.title");
	}
	
	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof GatedSpectrumRecipe gatedSpectrumRecipe) {
			return gatedSpectrumRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.getId().toString();
	}
	
	protected static ItemStack getDefaultStackWithCount(Item item, int count) {
		ItemStack stack = item.getDefaultStack();
		stack.setCount(count);
		return stack;
	}
	
}
