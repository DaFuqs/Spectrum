package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.blocks.potion_workshop.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class PotionWorkshopCraftingRecipe extends PotionWorkshopRecipe {
	
	protected final Ingredient baseIngredient;
	protected final boolean consumeBaseIngredient;
	protected final int requiredExperience;
	protected final ItemStack output;
	
	public PotionWorkshopCraftingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
	                                    Ingredient baseIngredient, boolean consumeBaseIngredient, int requiredExperience, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, ItemStack output, int craftingTime, int color) {
		
		super(id, group, secret, requiredAdvancementIdentifier, craftingTime, color, ingredient1, ingredient2, ingredient3);
		this.output = output;
		this.baseIngredient = baseIngredient;
		this.requiredExperience = requiredExperience;
		this.consumeBaseIngredient = consumeBaseIngredient;
		
		registerInToastManager(getType(), this);
	}
	
	public Ingredient getBaseIngredient() {
		return baseIngredient;
	}
	
	public boolean consumesBaseIngredient() {
		return consumeBaseIngredient;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public boolean usesReagents() {
		return false;
	}
	
	public int getRequiredExperience() {
		return this.requiredExperience;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}
	
	@Override
	public boolean isValidBaseIngredient(ItemStack itemStack) {
		return baseIngredient.test(itemStack);
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(Ingredient.ofStacks(SpectrumItems.MERMAIDS_GEM.getDefaultStack()));
		defaultedList.add(this.baseIngredient);
		defaultedList.add(this.ingredient1);
		defaultedList.add(this.ingredient2);
		defaultedList.add(this.ingredient3);
		return defaultedList;
	}
	
	@Override
	public boolean matches(@NotNull Inventory inv, World world) {
		if (enoughExperienceSupplied(inv)) {
			return super.matches(inv, world);
		}
		return false;
	}
	
	// we just test for a single ExperienceStorageItem here instead
	// of iterating over every item. The specification mentions that
	// Only one is supported and just a single ExperienceStorageItem
	// should be used per recipe, tough
	private boolean enoughExperienceSupplied(Inventory inv) {
		if (this.requiredExperience > 0) {
			for (int i : new int[]{PotionWorkshopBlockEntity.BASE_INPUT_SLOT_ID, PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT,
					PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT + 1, PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT + 2}) {
				
				if ((inv.getStack(i).getItem() instanceof ExperienceStorageItem)) {
					return ExperienceStorageItem.getStoredExperience(inv.getStack(i)) >= requiredExperience;
				}
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		return output;
	}
	
	@Override
	public int getMinOutputCount(ItemStack itemStack) {
		return 1;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING_ID;
	}
	
}
