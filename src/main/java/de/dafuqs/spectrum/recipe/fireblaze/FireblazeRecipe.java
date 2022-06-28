package de.dafuqs.spectrum.recipe.fireblaze;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireblazeRecipe implements Recipe<Inventory>, GatedRecipe {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_fireblaze");
	
	protected final Identifier id;
	protected final String group;
	
	protected final Ingredient inputIngredient;
	protected final int time;
	protected final float experience;
	protected final List<Pair<ItemStack, Float>> outputsWithChance;
	protected final Identifier requiredAdvancementIdentifier;

	public FireblazeRecipe(Identifier id, String group, Ingredient inputIngredient, int time, float experience, List<Pair<ItemStack, Float>> outputsWithChance, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.inputIngredient = inputIngredient;
		this.time = time;
		this.experience = experience;
		this.outputsWithChance = outputsWithChance;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
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
	public boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.FIREBLAZE);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.FIREBLAZE_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.FIREBLAZE;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient);
		return defaultedList;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof FireblazeRecipe fireblazeRecipe) {
			return fireblazeRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, UNLOCK_ADVANCEMENT_IDENTIFIER) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Nullable
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return this.requiredAdvancementIdentifier;
	}
	
	@Override
	public TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.fireblaze_recipe_unlocked.title");
	}
	
	@Override
	public TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.fireblaze_recipes_unlocked.title");
	}
	
	public float getExperience() {
		return this.experience;
	}
	
	public double getCraftingTime() {
		return this.time;
	}
	
	public List<ItemStack> getRolledOutputs(Random random) {
		List<ItemStack> output = new ArrayList<>();
		for(Pair<ItemStack, Float> possibleOutput : this.outputsWithChance) {
			float chance = possibleOutput.getRight();
			if(chance >= 1.0 || random.nextFloat() < chance) {
				output.add(possibleOutput.getLeft());
			}
		}
		return output;
	}
	
}
