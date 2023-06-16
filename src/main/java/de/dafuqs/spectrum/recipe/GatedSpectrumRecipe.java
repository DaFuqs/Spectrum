package de.dafuqs.spectrum.recipe;

import de.dafuqs.revelationary.api.advancements.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class GatedSpectrumRecipe implements Recipe<Inventory>, GatedRecipe {
	
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
		return AdvancementHelper.hasAdvancement(playerEntity, getRecipeTypeUnlockIdentifier()) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
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
		if (object instanceof GatedSpectrumRecipe) {
			return ((GatedSpectrumRecipe) object).getId().equals(this.getId());
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
	
	
	protected static boolean matchIngredientStacksExclusively(Inventory inv, List<IngredientStack> ingredientStacks) {
		if (inv.size() < ingredientStacks.size()) {
			return false;
		}
		
		int inputStackCount = 0;
		for (int i = 0; i < inv.size(); i++) {
			if (!inv.getStack(i).isEmpty()) {
				inputStackCount++;
			}
		}
		if (inputStackCount != ingredientStacks.size()) {
			return false;
		}
		
		
		for (IngredientStack ingredientStack : ingredientStacks) {
			boolean found = false;
			for (int i = 0; i < inv.size(); i++) {
				if (ingredientStack.test(inv.getStack(i))) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	protected static boolean matchIngredientStacksExclusively(Inventory inv, List<Ingredient> ingredients, int[] slots) {
		int inputStackCount = 0;
		for (int slot : slots) {
			if (!inv.getStack(slot).isEmpty()) {
				inputStackCount++;
			}
		}
		if (inputStackCount != ingredients.size()) {
			return false;
		}
		
		for (Ingredient ingredient : ingredients) {
			boolean found = false;
			for (int slot : slots) {
				if (ingredient.test(inv.getStack(slot))) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		
		return true;
	}
	
}
