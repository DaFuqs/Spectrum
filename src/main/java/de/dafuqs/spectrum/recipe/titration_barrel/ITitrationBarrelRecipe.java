package de.dafuqs.spectrum.recipe.titration_barrel;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.effect.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

/**
 * In contrast to most other Minecraft things, the titration barrel also counts the fermenting time
 * when the game is not running (comparing the time of sealing to the time of opening)
 * Making it a non-ticking block entity and also "fermenting" when the game is not running
 * This also means TitrationBarrelRecipes have to calculate their time using real life seconds, instead of game ticks
 */
public interface ITitrationBarrelRecipe<C extends FluidStackInventory> extends GatedRecipe<C> {
	
	Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/titration_barrel");
	
	// Called by the titration barrel when tapped
	default ItemStack getResult(Inventory inventory, long secondsFermented, float downfall) {
		// Dr. Who would be proud
		if (secondsFermented < 0) {
			float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
			;
			List<StatusEffectInstance> statusEffects = List.of(new StatusEffectInstance(StatusEffects.INVISIBILITY, 3600, 0));
			ItemStack stack = new StatusEffectBeverageProperties((long) ageIngameDays, 0, 0, statusEffects).getStack(SpectrumItems.SUSPICIOUS_BREW.getDefaultStack());
			LoreHelper.setLore(stack, Text.translatable("lore.spectrum.time_travel_tap"));
			return stack;
		}
		
		return tap(inventory, secondsFermented, downfall);
	}
	
	ItemStack tap(Inventory inventory, long secondsFermented, float downfall);
	
	Item getTappingItem();
	
	FluidIngredient getFluidInput();
	
	float getAngelsSharePerMcDay();
	
	// the amount of bottles able to get out of a single barrel
	default int getOutputCountAfterAngelsShare(World world, float temperature, long secondsFermented) {
		int originalOutputCount = getOutput(world.getRegistryManager()).getCount();

		if (getFermentationData() == null) {
			return originalOutputCount;
		}
		
		// Linearly adjust the output count based on angelsShareResultCount
		float angelsShareResultCount = getAngelsShareResultCount(secondsFermented, temperature);
		if (angelsShareResultCount > 0) {
			return Math.max(1, (int) Math.ceil((originalOutputCount - angelsShareResultCount)));
		} else {
			return Math.max(1, (int) Math.floor((originalOutputCount - angelsShareResultCount)));
		}
	}
	
	// the amount of fluid that evaporated while fermenting
	// the higher the temperature in the biome is, the more evaporates
	// making colder biomes more desirable
	default float getAngelsShareResultCount(long secondsFermented, float temperature) {
		return Math.max(0.1F, temperature / 10F) * TimeHelper.minecraftDaysFromSeconds(secondsFermented) * getAngelsSharePerMcDay();
	}
	
	@Override
	default boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	default ItemStack createIcon() {
		return SpectrumBlocks.TITRATION_BARREL.asItem().getDefaultStack();
	}
	
	@Override
	default RecipeType<?> getType() {
		return SpectrumRecipeTypes.TITRATION_BARREL;
	}
	
	List<IngredientStack> getIngredientStacks();
	
	int getMinFermentationTimeHours();
	
	default boolean isFermentingLongEnoughToTap(long secondsFermented) {
		return secondsFermented / 60 / 60 >= getMinFermentationTimeHours();
	}
	
	FermentationData getFermentationData();

}
