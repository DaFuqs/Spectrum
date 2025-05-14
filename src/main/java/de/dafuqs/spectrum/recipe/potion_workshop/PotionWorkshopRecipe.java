package de.dafuqs.spectrum.recipe.potion_workshop;


import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.potion_workshop.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class PotionWorkshopRecipe extends GatedStackSpectrumRecipe<RecipeInput> {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/potion_workshop");
	public static final int[] INGREDIENT_SLOTS = new int[]{2, 3, 4};
	
	protected final int craftingTime;
	protected final int color;
	
	protected final IngredientStack ingredient1;
	protected final IngredientStack ingredient2;
	protected final IngredientStack ingredient3;
	
	public PotionWorkshopRecipe(
			String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, int craftingTime, int color,
			IngredientStack ingredient1, IngredientStack ingredient2, IngredientStack ingredient3
	) {
		super(group, secret, requiredAdvancementIdentifier);
		this.color = color;
		this.craftingTime = craftingTime;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
	}
	
	public List<IngredientStack> getOtherIngredients() {
		ArrayList<IngredientStack> ingredients = new ArrayList<>();
		ingredients.add(ingredient1);
		if (!ingredient2.isEmpty()) {
			ingredients.add(ingredient2);
			if (!ingredient3.isEmpty()) {
				ingredients.add(ingredient3);
			}
		}
		return ingredients;
	}
	
	protected void addIngredientStacks(NonNullList<IngredientStack> ingredients) {
		ingredients.add(this.ingredient1);
		ingredients.add(this.ingredient2);
		ingredients.add(this.ingredient3);
	}
	
	@Override
	public boolean matches(@NotNull RecipeInput inv, Level world) {
		if (inv.size() > 4 && inv.getItem(0).is(SpectrumItems.MERMAIDS_GEM) && isValidBaseIngredient(inv.getItem(1))) {
			
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
	
	private boolean areStacksInReagentSlotsAllReagents(@NotNull RecipeInput inv) {
		for (int i : PotionWorkshopBlockEntity.REAGENT_SLOTS) {
			ItemStack itemStack = inv.getItem(i);
			if (!itemStack.isEmpty() && !PotionWorkshopReactingRecipe.isReagent(itemStack.getItem())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean areReagentSlotsEmpty(@NotNull RecipeInput inv) {
		for (int i : PotionWorkshopBlockEntity.REAGENT_SLOTS) {
			if (!inv.getItem(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public abstract boolean isValidBaseIngredient(ItemStack itemStack);
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultInstance();
	}
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.ANVIL_CRUSHING_RECIPE_SERIALIZER;
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
	public @Nullable ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	public int getRequiredExperience() {
		return 0;
	}
	
}
