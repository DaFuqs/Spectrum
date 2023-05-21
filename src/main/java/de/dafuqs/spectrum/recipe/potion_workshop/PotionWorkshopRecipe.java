package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class PotionWorkshopRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/potion_workshop");
	public static final int[] INGREDIENT_SLOTS = new int[]{2, 3, 4};
	
	protected final int craftingTime;
	protected final int color;
	
	protected final Ingredient ingredient1;
	protected final Ingredient ingredient2;
	protected final Ingredient ingredient3;
	
	public PotionWorkshopRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
	                            int craftingTime, int color, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.color = color;
		this.craftingTime = craftingTime;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
	}
	
	public List<Ingredient> getOtherIngredients() {
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(ingredient1);
		if (!ingredient2.isEmpty()) {
			ingredients.add(ingredient2);
			if (!ingredient3.isEmpty()) {
				ingredients.add(ingredient3);
			}
		}
		return ingredients;
	}
	
	@Override
	public boolean matches(@NotNull Inventory inv, World world) {
		if (inv.size() > 4 && inv.getStack(0).isOf(SpectrumItems.MERMAIDS_GEM) && isValidBaseIngredient(inv.getStack(1))) {
			
			if (usesReagents()) {
				if (!areStacksInReagentSlotsAllReagents(inv)) return false;
			} else {
				if (!areReagentSlotsEmpty(inv)) return false;
			}
			
			// check ingredients
			return matchIngredientStacksExclusively(inv, getOtherIngredients(), INGREDIENT_SLOTS);
		} else {
			return false;
		}
	}
	
	private boolean areStacksInReagentSlotsAllReagents(@NotNull Inventory inv) {
		for (int i : new int[]{5, 6, 7, 8}) {
			ItemStack itemStack = inv.getStack(i);
			if (!itemStack.isEmpty() && !PotionWorkshopReactingRecipe.isReagent(itemStack.getItem())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean areReagentSlotsEmpty(@NotNull Inventory inv) {
		for (int i : new int[]{5, 6, 7, 8}) {
			ItemStack itemStack = inv.getStack(i);
			if (!itemStack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public abstract boolean isValidBaseIngredient(ItemStack itemStack);
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack createIcon() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack();
	}
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING;
	}
	
	public abstract boolean usesReagents();
	
	public abstract int getMinOutputCount(ItemStack baseItemStack);
	
	public int getColor() {
		return this.color;
	}
	
	@Override
	public @Nullable Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
}
