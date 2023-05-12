package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.fluid.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
	protected final boolean yieldUpgradesDisabled;
	protected final boolean playCraftingFinishedEffects;
	
	protected final List<WorldConditionPredicate> worldConditions;
	@NotNull
	protected final FusionShrineRecipeWorldEffect startWorldEffect;
	@NotNull
	protected final List<FusionShrineRecipeWorldEffect> duringWorldEffects;
	@NotNull
	protected final FusionShrineRecipeWorldEffect finishWorldEffect;
	@Nullable
	protected final Text description;
	// copy all nbt data from the first stack in the ingredients to the output stack
	protected final boolean copyNbt;
	
	public FusionShrineRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
							  List<IngredientStack> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, boolean yieldUpgradesDisabled, boolean playCraftingFinishedEffects, boolean copyNbt,
							  List<WorldConditionPredicate> worldConditions, @NotNull FusionShrineRecipeWorldEffect startWorldEffect, @NotNull List<FusionShrineRecipeWorldEffect> duringWorldEffects, @NotNull FusionShrineRecipeWorldEffect finishWorldEffect, @Nullable Text description) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.craftingInputs = craftingInputs;
		this.fluidInput = fluidInput;
		this.output = output;
		this.experience = experience;
		this.craftingTime = craftingTime;
		this.yieldUpgradesDisabled = yieldUpgradesDisabled;
		this.playCraftingFinishedEffects = playCraftingFinishedEffects;
		
		this.worldConditions = worldConditions;
		this.startWorldEffect = startWorldEffect;
		this.duringWorldEffects = duringWorldEffects;
		this.finishWorldEffect = finishWorldEffect;
		this.description = description;
		this.copyNbt = copyNbt;

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
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager drm) {
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
	
	// should not be used. Instead, use getIngredientStacks(), which includes item counts
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
	public boolean areConditionMetCurrently(ServerWorld world, BlockPos pos) {
		for (WorldConditionPredicate worldCondition : this.worldConditions) {
			if (!worldCondition.test(world, pos)) {
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
				if (effect.isOneTimeEffect()) {
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

	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.FUSION_SHRINE_ID;
	}
	
	public void craft(World world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		ItemStack firstStack = ItemStack.EMPTY;

		int maxAmount = 1;
		if (!getOutput(world.getRegistryManager()).isEmpty()) {
			maxAmount = getOutput(world.getRegistryManager()).getMaxCount();
			for (IngredientStack ingredientStack : getIngredientStacks()) {
				for (int i = 0; i < fusionShrineBlockEntity.size(); i++) {
					ItemStack currentStack = fusionShrineBlockEntity.getStack(i);
					if (ingredientStack.test(currentStack)) {
						if (firstStack.isEmpty()) {
							firstStack = currentStack;
						}
						int ingredientStackAmount = ingredientStack.getCount();
						maxAmount = Math.min(maxAmount, currentStack.getCount() / ingredientStackAmount);
						break;
					}
				}
			}

			double efficiencyModifier = fusionShrineBlockEntity.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.EFFICIENCY);
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
				double efficiencyModifier = fusionShrineBlockEntity.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.EFFICIENCY);

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

		ItemStack output = getOutput(world.getRegistryManager()).copy();
		if (this.copyNbt) {
			// this overrides all nbt data, that are not nested compounds (like lists)
			NbtCompound sourceNbt = firstStack.getNbt();
			if (sourceNbt != null) {
				sourceNbt = sourceNbt.copy();
				sourceNbt.remove(ItemStack.DAMAGE_KEY);
				output.setNbt(sourceNbt);
				// so we need to restore all previous enchantments that the original item had and are still applicable to the new item
				output = SpectrumEnchantmentHelper.clearAndCombineEnchantments(output, false, false, getOutput(), firstStack);
			}
		}
		
		spawnCraftingResultAndXP(world, fusionShrineBlockEntity, output, maxAmount); // spawn results
	}
	
	protected void spawnCraftingResultAndXP(@NotNull World world, @NotNull FusionShrineBlockEntity fusionShrineBlockEntity, @NotNull ItemStack stack, int recipeCount) {
		int resultAmountBeforeMod = recipeCount * stack.getCount();
		double yieldModifier = yieldUpgradesDisabled ? 1.0 : fusionShrineBlockEntity.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.YIELD);
		int resultAmountAfterMod = Support.getIntFromDecimalWithChance(resultAmountBeforeMod * yieldModifier, world.random);
		
		int intExperience = Support.getIntFromDecimalWithChance(recipeCount * experience, world.random);
		MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, fusionShrineBlockEntity.getPos().up(2), stack, resultAmountAfterMod, MultiblockCrafter.RECIPE_STACK_VELOCITY);
		
		if (experience > 0) {
			MultiblockCrafter.spawnExperience(world, fusionShrineBlockEntity.getPos(), intExperience);
		}
		
		//only triggered on server side. Therefore, has to be sent to client via S2C packet
		fusionShrineBlockEntity.grantPlayerFusionCraftingAdvancement(this, intExperience);
	}
	
	public boolean shouldPlayCraftingFinishedEffects() {
		return this.playCraftingFinishedEffects;
	}
	
}
