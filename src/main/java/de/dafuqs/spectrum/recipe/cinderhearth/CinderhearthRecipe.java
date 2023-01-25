package de.dafuqs.spectrum.recipe.cinderhearth;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

import java.util.*;

public class CinderhearthRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("progression/unlock_cinderhearth");
	
	protected final Ingredient inputIngredient;
	protected final int time;
	protected final float experience;
	protected final List<Pair<ItemStack, Float>> outputsWithChance;
	
	public CinderhearthRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, int time, float experience, List<Pair<ItemStack, Float>> outputsWithChance) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.inputIngredient = inputIngredient;
		this.time = time;
		this.experience = experience;
		this.outputsWithChance = outputsWithChance;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return this.inputIngredient.test(inv.getStack(0));
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.CINDERHEARTH);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.CINDERHEARTH_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.CINDERHEARTH;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.CINDERHEARTH_ID;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient);
		return defaultedList;
	}
	
	public float getExperience() {
		return this.experience;
	}
	
	public int getCraftingTime() {
		return this.time;
	}
	
	public List<ItemStack> getRolledOutputs(Random random, float yieldMod) {
		List<ItemStack> output = new ArrayList<>();
		for (Pair<ItemStack, Float> possibleOutput : this.outputsWithChance) {
			float chance = possibleOutput.getRight();
			if (chance >= 1.0 || random.nextFloat() < chance * yieldMod) {
				ItemStack currentOutputStack = possibleOutput.getLeft();
				if (yieldMod > 1) {
					int totalCount = Support.getIntFromDecimalWithChance(currentOutputStack.getCount() * yieldMod, random);
					while (totalCount > 0) { // if the rolled count exceeds the max stack size we need to split them (unstackable items, counts > 64, ...)
						int count = Math.min(totalCount, currentOutputStack.getMaxCount());
						ItemStack outputStack = currentOutputStack.copy();
						outputStack.setCount(count);
						output.add(outputStack);
						totalCount -= count;
					}
				} else {
					output.add(currentOutputStack.copy());
				}
			}
		}
		return output;
	}
	
	public List<ItemStack> getPossibleOutputs() {
		List<ItemStack> outputs = new ArrayList<>();
		for (Pair<ItemStack, Float> pair : this.outputsWithChance) {
			outputs.add(pair.getLeft());
		}
		return outputs;
	}
	
	public List<Pair<ItemStack, Float>> getOutputsWithChance() {
		return outputsWithChance;
	}
	
}
