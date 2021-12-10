package de.dafuqs.spectrum.recipe.enchanter;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.progression.ClientRecipeToastManager;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldCondition;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldEffect;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnchanterRecipe implements Recipe<Inventory> {

	protected final Identifier id;
	protected final String group;
	
	protected final DefaultedList<Ingredient> inputs; // first input is the center, all others around clockwise
	protected final ItemStack output;
	
	protected final int requiredExperience;
	protected final int craftingTime;
	@Nullable protected final Identifier requiredAdvancementIdentifier;
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
		
		if(SpectrumClient.minecraftClient != null) {
			registerInClientToastManager();
		}
	}

	@Environment(EnvType.CLIENT)
	private void registerInClientToastManager() {
		ClientRecipeToastManager.registerUnlockableEnchanterRecipe(this);
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof EnchanterRecipe) {
			return ((EnchanterRecipe) object).getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		if(inv.size() > 8) {
			for(int i = 0; i < 9; i++) {
				if(!inputs.get(i).test(inv.getStack(i))) {
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

	public Identifier getId() {
		return this.id;
	}

	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ENCHANTER_RECIPE_SERIALIZER;
	}

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

}
