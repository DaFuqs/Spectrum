package de.dafuqs.spectrum.recipe.spirit_instiller;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class SpiritInstillerRecipe implements ISpiritInstillerRecipe {
	
	protected final Identifier id;
	protected final String group;
	
	protected final IngredientStack inputIngredient1;
	protected final IngredientStack inputIngredient2;
	protected final IngredientStack centerIngredient;
	protected final ItemStack outputItemStack;
	
	protected final int craftingTime;
	protected final float experience;
	protected final Identifier requiredAdvancementIdentifier;
	protected final boolean noBenefitsFromYieldAndEfficiencyUpgrades;

	public SpiritInstillerRecipe(Identifier id, String group, IngredientStack inputIngredient1, IngredientStack inputIngredient2, IngredientStack centerIngredient, ItemStack outputItemStack, int craftingTime, float experience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.inputIngredient1 = inputIngredient1;
		this.inputIngredient2 = inputIngredient2;
		this.centerIngredient = centerIngredient;
		this.outputItemStack = outputItemStack;
		this.craftingTime = craftingTime;
		this.experience = experience;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		this.noBenefitsFromYieldAndEfficiencyUpgrades = noBenefitsFromYieldAndEfficiencyUpgrades;
	}

	@Override
	public ItemStack getOutput() {
		return outputItemStack.copy();
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE_SERIALIZER;
	}

	@Deprecated
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient1.getIngredient());
		defaultedList.add(this.inputIngredient2.getIngredient());
		defaultedList.add(this.centerIngredient.getIngredient());
		return defaultedList;
	}
	
	public List<IngredientStack> getIngredientStacks() {
		DefaultedList<IngredientStack> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient1);
		defaultedList.add(this.inputIngredient2);
		defaultedList.add(this.centerIngredient);
		return defaultedList;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof SpiritInstillerRecipe spiritInstillerRecipe) {
			return spiritInstillerRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public String getGroup() {
		return this.group;
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
	
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return noBenefitsFromYieldAndEfficiencyUpgrades;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, UNLOCK_SPIRIT_INSTILLER_ADVANCEMENT_IDENTIFIER) && Support.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
}
