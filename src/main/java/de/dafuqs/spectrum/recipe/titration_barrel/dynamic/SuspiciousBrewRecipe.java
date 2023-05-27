package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.food.beverages.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.block.*;
import net.minecraft.entity.effect.*;
import net.minecraft.fluid.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class SuspiciousBrewRecipe extends TitrationBarrelRecipe {


	public static final RecipeSerializer<SuspiciousBrewRecipe> SERIALIZER = new EmptyRecipeSerializer<>(SuspiciousBrewRecipe::new);
	public static final Item TAPPING_ITEM = Items.GLASS_BOTTLE;
	public static final int MIN_FERMENTATION_TIME_HOURS = 4;
	public static final ItemStack OUTPUT_STACK = getDefaultStackWithCount(SpectrumItems.SUSPICIOUS_BREW, 4);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/food/suspicious_brew");
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.of(Ingredient.fromTag(ItemTags.SMALL_FLOWERS)));
		add(IngredientStack.of(Ingredient.fromTag(ItemTags.SMALL_FLOWERS)));
		add(IngredientStack.of(Ingredient.fromTag(ItemTags.SMALL_FLOWERS)));
		add(IngredientStack.of(Ingredient.fromTag(ItemTags.SMALL_FLOWERS)));
	}};
	
	public SuspiciousBrewRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER, INGREDIENT_STACKS, Fluids.WATER, OUTPUT_STACK, TAPPING_ITEM, MIN_FERMENTATION_TIME_HOURS, new FermentationData(1.0F, 0.1F, List.of()));
	}

	@Override
	public ItemStack getDefaultTap(int timeMultiplier) {
		ItemStack flowerStack = Items.POPPY.getDefaultStack();
		flowerStack.setCount(4);
		ItemStack tappedStack = tapWith(List.of(flowerStack), 1.0F, this.minFermentationTimeHours * 60L * 60L * timeMultiplier, 0.4F); // downfall equals the one in plains
		BeverageItem.setPreviewStack(tappedStack);
		tappedStack.setCount(OUTPUT_STACK.getCount());
		return tappedStack;
	}
	
	@Override
	public ItemStack tap(Inventory inventory, long secondsFermented, float downfall) {
		List<ItemStack> stacks = new ArrayList<>();
		int itemCount = 0;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (!stack.isEmpty()) {
				stacks.add(stack);
				itemCount += stack.getCount();
			}
		}
		float thickness = getThickness(itemCount);
		return tapWith(stacks, thickness, secondsFermented, downfall);
	}

	public ItemStack tapWith(List<ItemStack> stacks, float thickness, long secondsFermented, float downfall) {
		if (secondsFermented / 60 / 60 < this.minFermentationTimeHours) {
			return NOT_FERMENTED_LONG_ENOUGH_OUTPUT_STACK;
		}
		
		float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
		double alcPercent = getAlcPercent(this.fermentationData.fermentationSpeedMod(), thickness, downfall, ageIngameDays);
		if (alcPercent >= 100) {
			return getPureAlcohol(ageIngameDays);
		} else {
			// add up all stew effects with their durations from the input stacks
			Map<StatusEffect, Integer> stewEffects = new HashMap<>();
			for (ItemStack stack : stacks) {
				Optional<Pair<StatusEffect, Integer>> stewEffect = getStewEffectFrom(stack);
				if (stewEffect.isPresent()) {
					StatusEffect effect = stewEffect.get().getLeft();
					int duration = (int) (stewEffect.get().getRight() * (1 + Support.logBase(stack.getCount(), 2)));
					if (stewEffects.containsKey(effect)) {
						stewEffects.put(effect, stewEffects.get(effect) + duration);
					} else {
						stewEffects.put(effect, duration);
					}
				}
			}
			
			List<StatusEffectInstance> finalStatusEffects = new ArrayList<>();
			double cappedAlcPercent = Math.min(alcPercent, 20D);
			for (Map.Entry<StatusEffect, Integer> entry : stewEffects.entrySet()) {
				int finalDurationTicks = (int) (entry.getValue() * Math.pow(2, 1 + cappedAlcPercent));
				finalStatusEffects.add(new StatusEffectInstance(entry.getKey(), finalDurationTicks, 0));
			}
			
			ItemStack outputStack = OUTPUT_STACK.copy();
			outputStack.setCount(1);
			return new StatusEffectBeverageProperties((long) ageIngameDays, (int) alcPercent, thickness, finalStatusEffects).getStack(outputStack);
		}
	}
	
	// taken from SuspiciousStewItem
	private Optional<Pair<StatusEffect, Integer>> getStewEffectFrom(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (block instanceof FlowerBlock flowerBlock) {
				return Optional.of(Pair.of(flowerBlock.getEffectInStew(), flowerBlock.getEffectInStewDuration()));
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		boolean flowerFound = false;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.isIn(ItemTags.SMALL_FLOWERS)) {
					flowerFound = true;
				} else {
					return false;
				}
			}
		}

		return flowerFound;
	}

	@Override
	public RecipeSerializer<?> getSerializer()  {
		return SERIALIZER;
	}
}
