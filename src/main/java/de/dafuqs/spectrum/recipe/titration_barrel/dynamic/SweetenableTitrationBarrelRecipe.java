package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.effect.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public abstract class SweetenableTitrationBarrelRecipe extends TitrationBarrelRecipe {
	
	public SweetenableTitrationBarrelRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> inputStacks, FluidIngredient fluid, ItemStack outputItemStack, Item tappingItem, int minFermentationTimeHours, FermentationData fermentationData) {
		super(id, group, secret, requiredAdvancementIdentifier, inputStacks, fluid, outputItemStack, tappingItem, minFermentationTimeHours, fermentationData);
	}
	
	@Override
	public ItemStack getPreviewTap(int timeMultiplier) {
		return tapWith(1, 3, false, 1.0F, this.minFermentationTimeHours * 60L * 60L * timeMultiplier, 0.4F);
	}
	
	protected abstract List<StatusEffectInstance> getEffects(boolean nectar, double bloominess, double alcPercent);
	
	@Override
	public ItemStack tap(Inventory inventory, long secondsFermented, float downfall) {
		int bulbCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.JADEITE_LOTUS_BULB);
		int petalCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.JADEITE_PETALS);
		boolean nectar = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
		
		float thickness = getThickness(bulbCount, petalCount);
		return tapWith(bulbCount, petalCount, nectar, thickness, secondsFermented, downfall);
	}
	
	protected ItemStack tapWith(int bulbCount, int petalCount, boolean nectar, float thickness, long secondsFermented, float downfall) {
		double bloominess = getBloominess(bulbCount, petalCount);
		float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
		if (nectar) {
			thickness *= 1.5F;
		}
		double alcPercent = getAlcPercentWithBloominess(ageIngameDays, downfall, bloominess, thickness);
		if (alcPercent >= 100) {
			return SpectrumItems.CHRYSOCOLLA.getDefaultStack();
		} else {
			List<StatusEffectInstance> effects = getEffects(nectar, bloominess, alcPercent);
			
			ItemStack outputStack = outputItemStack.copy();
			outputStack.setCount(1);
			return new JadeWineBeverageProperties((long) ageIngameDays, (int) alcPercent, thickness, (float) bloominess, nectar, effects).getStack(outputStack);
		}
	}
	
	// bloominess reduces the possibility of negative effects to trigger (better on the tongue)
	// but also reduces the potency of positive effects a bit
	protected static double getBloominess(int bulbCount, int petalCount) {
		if (bulbCount == 0) {
			return 0;
		}
		return (double) petalCount / (double) bulbCount / 2F;
	}
	
	// the amount of solid to liquid
	protected float getThickness(int bulbCount, int petalCount) {
		return bulbCount + petalCount / 8F;
	}
	
	// the alc % determines the power of effects when drunk
	// it generally increases the longer the wine has fermented
	//
	// another detail: the more rainy the weather (downfall) the more water evaporates
	// compared to alcohol, making the drink stronger / weaker in return
	protected double getAlcPercentWithBloominess(float ageIngameDays, float downfall, double bloominess, double thickness) {
		return Support.logBase(1 + this.fermentationData.fermentationSpeedMod() + bloominess / 64, ageIngameDays * (0.5 + thickness / 2) * (0.5D + downfall / 2D));
	}
	
}
