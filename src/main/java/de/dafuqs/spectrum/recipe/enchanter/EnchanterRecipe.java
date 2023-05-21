package de.dafuqs.spectrum.recipe.enchanter;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;

public class EnchanterRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_enchanting_structure");
	
	protected final DefaultedList<Ingredient> inputs; // first input is the center, all others around clockwise
	protected final ItemStack output;
	
	protected final int requiredExperience;
	protected final int craftingTime;
	protected final boolean noBenefitsFromYieldAndEfficiencyUpgrades;
	
	public EnchanterRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, DefaultedList<Ingredient> inputs, ItemStack output, int craftingTime, int requiredExperience, boolean noBenefitsFromYieldAndEfficiencyUpgrades) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.inputs = inputs;
		this.output = output;
		this.requiredExperience = requiredExperience;
		this.craftingTime = craftingTime;
		this.noBenefitsFromYieldAndEfficiencyUpgrades = noBenefitsFromYieldAndEfficiencyUpgrades;
		
		registerInToastManager(getType(), this);
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
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.ENCHANTER);
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
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
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return noBenefitsFromYieldAndEfficiencyUpgrades;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.ENCHANTER_ID;
	}
	
}
