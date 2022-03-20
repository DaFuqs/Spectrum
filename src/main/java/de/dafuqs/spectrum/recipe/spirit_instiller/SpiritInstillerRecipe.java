package de.dafuqs.spectrum.recipe.spirit_instiller;

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

public class SpiritInstillerRecipe implements Recipe<Inventory> {

	protected final Identifier id;
	protected final Ingredient inputIngredient1;
	protected final Ingredient inputIngredient2;
	protected final Ingredient centerIngredient;
	protected final ItemStack outputItemStack;
	
	protected final int craftingTime;
	protected final float experience;
	protected final Identifier requiredAdvancementIdentifier;

	public SpiritInstillerRecipe(Identifier id, Ingredient inputIngredient1, Ingredient inputIngredient2, Ingredient centerIngredient, ItemStack outputItemStack, int craftingTime, float experience, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.inputIngredient1 = inputIngredient1;
		this.inputIngredient2 = inputIngredient2;
		this.centerIngredient = centerIngredient;
		this.outputItemStack = outputItemStack;
		this.craftingTime = craftingTime;
		this.experience = experience;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		if(inv.size() > 2) {
			if(inputIngredient1.test(inv.getStack(0)) && inputIngredient2.test(inv.getStack(1))) {
				return centerIngredient.test(inv.getStack(2));
			} else if(inputIngredient1.test(inv.getStack(1)) && inputIngredient2.test(inv.getStack(0))) {
				return centerIngredient.test(inv.getStack(2));
			}
		}
		return false;
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return null;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return outputItemStack.copy();
	}
	
	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.SPIRIT_INSTILLER);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient1);
		defaultedList.add(this.inputIngredient2);
		defaultedList.add(this.centerIngredient);
		return defaultedList;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof SpiritInstillerRecipe anvilCrushingRecipe) {
			return anvilCrushingRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
	public float getExperience() {
		return experience;
	}
	
	public int getCraftingTime() {
		return craftingTime;
	}
	
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
}
