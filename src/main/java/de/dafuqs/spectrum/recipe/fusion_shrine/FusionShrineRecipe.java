package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FusionShrineRecipe implements Recipe<Inventory>, GatedRecipe {
	
	protected final Identifier id;
	protected final String group;
	
	protected final List<IngredientStack> craftingInputs;
	protected final Fluid fluidInput;
	protected final ItemStack output;
	protected final float experience;
	protected final int craftingTime;
	// since there are a few recipes that are basically compacting recipes
	// they could be crafted ingots>block and block>ingots back
	// In that case:
	// - the player should not get XP
	// - Yield upgrades disabled (item multiplication)
	protected final boolean noBenefitsFromYieldUpgrades;
	
	protected final List<FusionShrineRecipeWorldCondition> worldConditions;
	@NotNull
	protected final FusionShrineRecipeWorldEffect startWorldEffect;
	@NotNull
	protected final List<FusionShrineRecipeWorldEffect> duringWorldEffects;
	@NotNull
	protected final FusionShrineRecipeWorldEffect finishWorldEffect;
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;
	@Nullable
	protected final Text description;
	
	public FusionShrineRecipe(Identifier id, String group, List<IngredientStack> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, boolean noBenefitsFromYieldUpgrades, Identifier requiredAdvancementIdentifier,
	                          List<FusionShrineRecipeWorldCondition> worldConditions, FusionShrineRecipeWorldEffect startWorldEffect, List<FusionShrineRecipeWorldEffect> duringWorldEffects, FusionShrineRecipeWorldEffect finishWorldEffect, Text description) {
		this.id = id;
		this.group = group;
		
		this.craftingInputs = craftingInputs;
		this.fluidInput = fluidInput;
		this.output = output;
		this.experience = experience;
		this.craftingTime = craftingTime;
		this.noBenefitsFromYieldUpgrades = noBenefitsFromYieldUpgrades;
		
		this.worldConditions = worldConditions;
		this.startWorldEffect = startWorldEffect;
		this.duringWorldEffects = duringWorldEffects;
		this.finishWorldEffect = finishWorldEffect;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		this.description = description;
		
		registerInToastManager(SpectrumRecipeTypes.FUSION_SHRINE, this);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof FusionShrineRecipe) {
			return ((FusionShrineRecipe) object).getId().equals(this.getId());
		}
		return false;
	}
	
	/**
	 * Only tests the items. The required fluid has to be tested manually by the crafting block
	 */
	@Override
	public boolean matches(Inventory inv, World world) {
		List<IngredientStack> ingredientStacks = this.getIngredientStacks();
		if (inv.size() < ingredientStacks.size()) {
			return false;
		}
		
		int inputStackCount = 0;
		for (int i = 0; i < inv.size(); i++) {
			if (!inv.getStack(i).isEmpty()) {
				inputStackCount++;
			}
		}
		if (inputStackCount != ingredientStacks.size()) {
			return false;
		}
		
		
		for (IngredientStack ingredientStack : ingredientStacks) {
			boolean found = false;
			for (int i = 0; i < inv.size(); i++) {
				inputStackCount++;
				if (ingredientStack.test(inv.getStack(i))) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
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
		return new ItemStack(SpectrumBlocks.FUSION_SHRINE_BASALT);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.FUSION_SHRINE_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.FUSION_SHRINE;
	}
	
	// should not be used. Instead use getIngredientStacks(), which includes item counts
	@Override
	@Deprecated
	public DefaultedList<Ingredient> getIngredients() {
		return IngredientStack.listIngredients(this.craftingInputs);
	}
	
	public List<IngredientStack> getIngredientStacks() {
		return this.craftingInputs;
	}
	
	public float getExperience() {
		return experience;
	}
	
	/**
	 * The advancement the player has to have to let the recipe be craftable
	 *
	 * @return The advancement identifier. A null value means the player is always able to craft this recipe
	 */
	@Nullable
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
	/**
	 * Returns a boolean depending on if the recipes condition is set
	 * This can be always true, a specific day or moon phase, or weather.
	 */
	public boolean areConditionMetCurrently(World world) {
		for (FusionShrineRecipeWorldCondition worldCondition : this.worldConditions) {
			if (!worldCondition.isMetCurrently(world)) {
				return false;
			}
		}
		return true;
	}
	
	public Fluid getFluidInput() {
		return this.fluidInput;
	}
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	/**
	 * @param tick The crafting tick if the fusion shrine recipe
	 * @return The effect that should be played for the given recipe tick
	 */
	public FusionShrineRecipeWorldEffect getWorldEffectForTick(int tick, int totalTicks) {
		if (tick == 1) {
			return this.startWorldEffect;
		} else if (tick == totalTicks) {
			return this.finishWorldEffect;
		} else {
			if (this.duringWorldEffects.size() == 0) {
				return null;
			} else if (this.duringWorldEffects.size() == 1) {
				return this.duringWorldEffects.get(0);
			} else {
				// we really have to calculate the current effect, huh?
				float parts = (float) totalTicks / this.duringWorldEffects.size();
				int index = (int) (tick / (parts));
				FusionShrineRecipeWorldEffect effect = this.duringWorldEffects.get(index);
				if (effect.isOneTimeEffect(effect)) {
					if (index != (int) parts) {
						return null;
					}
				}
				return effect;
			}
		}
	}
	
	public Optional<Text> getDescription() {
		if (this.description == null) {
			return Optional.empty();
		} else {
			return Optional.of(this.description);
		}
	}
	
	public boolean areYieldUpgradesDisabled() {
		return noBenefitsFromYieldUpgrades;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.fusion_shrine_recipe_unlocked.title");
	}
	
	@Override
	public TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.fusion_shrine_recipes_unlocked.title");
	}
	
}
