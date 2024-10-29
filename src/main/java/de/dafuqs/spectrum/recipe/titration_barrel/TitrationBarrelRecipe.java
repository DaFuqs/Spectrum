package de.dafuqs.spectrum.recipe.titration_barrel;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.titration_barrel.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.entity.effect.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TitrationBarrelRecipe extends GatedStackSpectrumRecipe<TitrationBarrelBlockEntity> implements ITitrationBarrelRecipe {
	
	public static final List<Integer> FERMENTATION_DURATION_DISPLAY_TIME_MULTIPLIERS = new ArrayList<>() {{
		add(1);
		add(10);
		add(100);
	}};
	
	protected final List<IngredientStack> inputStacks;
	protected final ItemStack outputItemStack;
	protected final Item tappingItem;
	protected final FluidIngredient fluid;
	
	protected final int minFermentationTimeHours;
	protected final FermentationData fermentationData;
	
	public TitrationBarrelRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> inputStacks, FluidIngredient fluid, ItemStack outputItemStack, Item tappingItem, int minFermentationTimeHours, FermentationData fermentationData) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.inputStacks = inputStacks;
		this.fluid = fluid;
		this.minFermentationTimeHours = minFermentationTimeHours;
		this.outputItemStack = outputItemStack;
		this.tappingItem = tappingItem;
		this.fermentationData = fermentationData;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(TitrationBarrelBlockEntity inventory, World world) {
		SingleVariantStorage<FluidVariant> fluidStorage = inventory.getFluidStorage();
		if (!this.fluid.test(fluidStorage.variant)) {
			return false;
		}
		if (this.fluid != FluidIngredient.EMPTY) {
			if (fluidStorage.getAmount() != fluidStorage.getCapacity()) {
				return false;
			}
		}
		return matchIngredientStacksExclusively(inventory, getIngredientStacks());
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		return this.inputStacks;
	}
	
	@Override
	public Item getTappingItem() {
		return tappingItem;
	}
	
	@Override
	public int getMinFermentationTimeHours() {
		return this.minFermentationTimeHours;
	}
	
	@Override
	public FermentationData getFermentationData() {
		return this.fermentationData;
	}
	
	@Override
	@Deprecated
	public ItemStack craft(TitrationBarrelBlockEntity inventory, DynamicRegistryManager drm) {
		return getDefaultTap(1).copy();
	}
	
	public ItemStack getPreviewTap(int timeMultiplier) {
		return tapWith(1.0F, this.minFermentationTimeHours * 60L * 60L * timeMultiplier, 0.4F); // downfall equals the one in plains
	}
	
	public ItemStack getDefaultTap(int timeMultiplier) {
		ItemStack stack = getPreviewTap(timeMultiplier);
		stack.setCount(this.outputItemStack.getCount());
		FermentedItem.setPreviewStack(stack);
		return stack;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager registryManager) {
		return getDefaultTap(1);
	}
	
	// used for recipe viewers to show recipe outputs with a few example fermentation times
	public Collection<ItemStack> getOutputVariations(List<Integer> timeMultipliers) {
		List<ItemStack> list = new ArrayList<>();
		for (int timeMultiplier : timeMultipliers) {
			list.add(getDefaultTap(timeMultiplier));
		}
		return list;
	}
	
	@Override
	public FluidIngredient getFluidInput() {
		return fluid;
	}
	
	@Override
	public float getAngelsSharePerMcDay() {
		if (this.fermentationData == null) {
			return 0;
		}
		return this.fermentationData.angelsSharePercentPerMcDay();
	}
	
	@Override
	public ItemStack tap(Inventory inventory, long secondsFermented, float downfall) {
		int contentCount = InventoryHelper.countItemsInInventory(inventory);
		float thickness = getThickness(contentCount);
		return tapWith(thickness, secondsFermented, downfall);
	}
	
	private ItemStack tapWith(float thickness, long secondsFermented, float downfall) {
		ItemStack stack = this.outputItemStack.copy();
		stack.setCount(1);
		
		if (this.fermentationData == null) {
			return stack;
		} else {
			return getFermentedStack(this.fermentationData, thickness, secondsFermented, downfall, stack);
		}
	}
	
	public static ItemStack getFermentedStack(@NotNull FermentationData fermentationData, float thickness, long secondsFermented, float downfall, ItemStack inputStack) {
		float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
		double alcPercent = 0;
		if (fermentationData.fermentationSpeedMod() > 0) {
			alcPercent = getAlcPercent(fermentationData.fermentationSpeedMod(), thickness, downfall, ageIngameDays);
			alcPercent = Math.max(0, alcPercent);
		}
		
		if (alcPercent >= 100) {
			return SpectrumItems.PURE_ALCOHOL.getDefaultStack();
		}
		
		BeverageProperties properties;
		if (inputStack.getItem() instanceof FermentedItem fermentedItem) {
			properties = fermentedItem.getBeverageProperties(inputStack);
		} else {
			// if it's not a set beverage (custom recipe) assume VariantBeverage to add that tag
			properties = VariantBeverageProperties.getFromStack(inputStack);
		}
		
		if (properties instanceof StatusEffectBeverageProperties statusEffectBeverageProperties) {
			float durationMultiplier = (float) (Support.logBase(1 + thickness, 2));
			
			List<StatusEffectInstance> effects = new ArrayList<>();
			for (FermentationStatusEffectEntry entry : fermentationData.statusEffectEntries()) {
				int potency = -1;
				int durationTicks = entry.baseDuration();
				for (FermentationStatusEffectEntry.StatusEffectPotencyEntry potencyEntry : entry.potencyEntries()) {
					if (thickness >= potencyEntry.minThickness() && alcPercent >= potencyEntry.minAlcPercent()) {
						potency = potencyEntry.potency();
					}
				}
				if (potency > -1) {
					effects.add(new StatusEffectInstance(entry.statusEffect(), (int) (durationTicks * durationMultiplier), potency));
				}
			}
			
			statusEffectBeverageProperties.statusEffects = effects;
		}
		
		properties.alcPercent = (int) alcPercent;
		properties.ageDays = (long) ageIngameDays;
		properties.thickness = thickness;
		return properties.getStack(inputStack);
	}
	
	protected static double getAlcPercent(float fermentationSpeedMod, float thickness, float downfall, float ageIngameDays) {
		return Support.logBase(1 + fermentationSpeedMod, ageIngameDays * (0.5D + thickness / 2D) * (0.5D + downfall / 2D));
	}
	
	protected float getThickness(int contentCount) {
		int inputStacksCount = 0;
		for (IngredientStack stack : inputStacks) {
			inputStacksCount += stack.getCount();
		}
		return contentCount / (float) inputStacksCount;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.TITRATION_BARREL_RECIPE_SERIALIZER;
	}
	
	// sadly we cannot use text.append() here, since the guidebook does not support it
	// but this way it might be easier for translations either way
	public static MutableText getDurationText(int minFermentationTimeHours, FermentationData fermentationData) {
		MutableText text;
		if (fermentationData == null) {
			if (minFermentationTimeHours == 1) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_hour");
			} else if (minFermentationTimeHours == 24) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_day");
			} else if (minFermentationTimeHours >= 72) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours / 24F));
			} else {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_hours", minFermentationTimeHours);
			}
		} else {
			if (minFermentationTimeHours == 1) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_hour");
			} else if (minFermentationTimeHours == 24) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_day");
			} else if (minFermentationTimeHours > 72) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours / 24F));
			} else {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_hours", minFermentationTimeHours);
			}
		}
		return text;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return ITitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.TITRATION_BARREL_ID;
	}
	
}
