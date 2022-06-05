package de.dafuqs.spectrum.recipe.enchanter;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnchanterRecipe implements Recipe<Inventory>, GatedRecipe {
	
	public static final Identifier UNLOCK_ENCHANTING_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/build_enchanting_structure");
	
	protected final Identifier id;
	protected final String group;
	
	protected final DefaultedList<Ingredient> inputs; // first input is the center, all others around clockwise
	protected final ItemStack output;
	
	protected final int requiredExperience;
	protected final int craftingTime;
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;
	protected final boolean noBenefitsFromYieldAndEfficiencyUpgrades;
	
	public EnchanterRecipe(Identifier id, String group, DefaultedList<Ingredient> inputs, ItemStack output, int craftingTime, int requiredExperience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, @Nullable Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		
		this.inputs = inputs;
		this.output = output;
		this.requiredExperience = requiredExperience;
		this.craftingTime = craftingTime;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		this.noBenefitsFromYieldAndEfficiencyUpgrades = noBenefitsFromYieldAndEfficiencyUpgrades;
		
		registerInToastManager(SpectrumRecipeTypes.ENCHANTER, this);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof EnchanterRecipe) {
			return ((EnchanterRecipe) object).getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		if (inv.size() > 9) {
			if (!inputs.get(0).test(inv.getStack(0))) {
				return false;
			}
			if (this.getRequiredExperience() > 0
					&& !(inv.getStack(1).getItem() instanceof ExperienceStorageItem)
					&& ExperienceStorageItem.getStoredExperience(inv.getStack(1)) < this.getRequiredExperience()) {
				return false;
			}
			
			for (int i = 1; i < 9; i++) {
				if (!inputs.get(i).test(inv.getStack(i + 1))) {
					return false;
				}
			}
			
			return true;
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
		return output;
	}
	
	public boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.ENCHANTER);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ENCHANTER_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ENCHANTER;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return inputs;
	}
	
	public int getRequiredExperience() {
		return requiredExperience;
	}
	
	/**
	 * The advancement the player has to have to let the recipe be craftable in the pedestal
	 *
	 * @return The advancement identifier. A null value means the player is always able to craft this recipe
	 */
	@Nullable
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return noBenefitsFromYieldAndEfficiencyUpgrades;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, EnchanterRecipe.UNLOCK_ENCHANTING_ADVANCEMENT_IDENTIFIER) && Support.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.enchanter_recipe_unlocked.title");
	}
	
	@Override
	public TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.enchanter_recipes_unlocked.title");
	}
	
}
