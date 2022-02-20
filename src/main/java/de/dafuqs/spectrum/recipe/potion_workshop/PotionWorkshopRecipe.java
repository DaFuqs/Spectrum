package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class PotionWorkshopRecipe implements Recipe<Inventory>, GatedRecipe {

	protected final Identifier id;
	protected final String group;
	
	protected final Ingredient baseIngredient;
	protected final boolean consumeBaseIngredient;
	protected final Ingredient ingredient1;
	protected final Ingredient ingredient2;
	protected final Ingredient ingredient3;
	
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;

	public PotionWorkshopRecipe(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.baseIngredient = baseIngredient;
		this.consumeBaseIngredient = consumeBaseIngredient;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}

	public boolean matches(Inventory inv, World world) {
		// TODO
		return true;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public ItemStack createIcon() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack();
	}

	public Identifier getId() {
		return this.id;
	}

	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.baseIngredient);
		defaultedList.add(this.ingredient1);
		defaultedList.add(this.ingredient2);
		defaultedList.add(this.ingredient3);
		return defaultedList;
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof PotionWorkshopRecipe) {
			return ((PotionWorkshopRecipe) object).getId().equals(this.getId());
		}
		return false;
	}

}
