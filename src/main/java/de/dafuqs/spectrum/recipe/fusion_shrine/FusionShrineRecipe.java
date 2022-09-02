package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.GatedSpectrumRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FusionShrineRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("build_fusion_shrine");
	
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
	protected final Text description;
	
	public FusionShrineRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
	                          List<IngredientStack> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, boolean noBenefitsFromYieldUpgrades,
	                          List<FusionShrineRecipeWorldCondition> worldConditions, FusionShrineRecipeWorldEffect startWorldEffect, List<FusionShrineRecipeWorldEffect> duringWorldEffects, FusionShrineRecipeWorldEffect finishWorldEffect, Text description) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
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
		this.description = description;
		
		registerInToastManager(getType(), this);
	}
	
	/**
	 * Only tests the items. The required fluid has to be tested manually by the crafting block
	 */
	@Override
	public boolean matches(Inventory inv, World world) {
		return matchIngredientStacksExclusively(inv, getIngredientStacks());
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
		return output;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.FUSION_SHRINE_BASALT);
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
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.FUSION_SHRINE_ID;
	}
	
	public void craft(World world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		int maxAmount = 1;
		if (!getOutput().isEmpty()) {
			maxAmount = getOutput().getMaxCount();
			for (IngredientStack ingredientStack : getIngredientStacks()) {
				for (int i = 0; i < fusionShrineBlockEntity.size(); i++) {
					ItemStack currentStack = fusionShrineBlockEntity.getStack(i);
					if (ingredientStack.test(currentStack)) {
						int ingredientStackAmount = ingredientStack.getCount();
						maxAmount = Math.min(maxAmount, currentStack.getCount() / ingredientStackAmount);
						break;
					}
				}
			}
			
			double efficiencyModifier = fusionShrineBlockEntity.getUpgrades().get(Upgradeable.UpgradeType.EFFICIENCY);
			if (maxAmount > 0) {
				for (IngredientStack ingredientStack : getIngredientStacks()) {
					for (int i = 0; i < fusionShrineBlockEntity.size(); i++) {
						ItemStack currentStack = fusionShrineBlockEntity.getStack(i);
						if (ingredientStack.test(currentStack)) {
							int reducedAmount = maxAmount * ingredientStack.getCount();
							int reducedAmountAfterMod = Support.getIntFromDecimalWithChance(reducedAmount / efficiencyModifier, world.random);
							if (currentStack.getCount() - reducedAmountAfterMod < 1) {
								fusionShrineBlockEntity.setStack(i, ItemStack.EMPTY);
							} else {
								currentStack.decrement(reducedAmountAfterMod);
							}
							break;
						}
					}
				}
			}
		} else {
			for (IngredientStack ingredientStack : getIngredientStacks()) {
				double efficiencyModifier = fusionShrineBlockEntity.getUpgrades().get(Upgradeable.UpgradeType.EFFICIENCY);
				
				for (int i = 0; i < fusionShrineBlockEntity.size(); i++) {
					ItemStack currentStack = fusionShrineBlockEntity.getStack(i);
					if (ingredientStack.test(currentStack)) {
						int reducedAmountAfterMod = Support.getIntFromDecimalWithChance(ingredientStack.getCount() / efficiencyModifier, world.random);
						currentStack.decrement(reducedAmountAfterMod);
						break;
					}
				}
			}
		}
		
		fusionShrineBlockEntity.setFluid(Fluids.EMPTY); // empty the shrine
		FusionShrineBlockEntity.spawnCraftingResultAndXP(world, fusionShrineBlockEntity, this, maxAmount); // spawn results
	}
	
}
