package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class SuspiciousBrewRecipe extends TitrationBarrelRecipe {
	
	public static final Item TAPPING_ITEM = Items.GLASS_BOTTLE;
	public static final int MIN_FERMENTATION_TIME_HOURS = 4;
	public static final ItemStack OUTPUT_STACK = getDefaultStackWithCount(SpectrumItems.SUSPICIOUS_BREW, 4);
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/food/suspicious_brew");
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.ofTag(ItemTags.SMALL_FLOWERS, 1));
		add(IngredientStack.ofTag(ItemTags.SMALL_FLOWERS, 1));
		add(IngredientStack.ofTag(ItemTags.SMALL_FLOWERS, 1));
		add(IngredientStack.ofTag(ItemTags.SMALL_FLOWERS, 1));
	}};
	
	public SuspiciousBrewRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER), INGREDIENT_STACKS, FluidIngredient.of(Fluids.WATER), OUTPUT_STACK, TAPPING_ITEM, MIN_FERMENTATION_TIME_HOURS, new FermentationData(1.25F, 0.01F, List.of()));
	}
	
	@Override
	public ItemStack getPreviewTap(int timeMultiplier) {
		ItemStack flowerStack = Items.POPPY.getDefaultInstance();
		flowerStack.setCount(4);
		return tapWith(List.of(flowerStack), 1.0F, this.minFermentationTimeHours * 60L * 60L * timeMultiplier, 0.4F);
	}
	
	@Override
	public ItemStack tap(Container inventory, long secondsFermented, float downfall) {
		List<ItemStack> stacks = new ArrayList<>();
		int itemCount = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (!stack.isEmpty()) {
				stacks.add(stack);
				itemCount += stack.getCount();
			}
		}
		float thickness = getThickness(itemCount);
		return tapWith(stacks, thickness, secondsFermented, downfall);
	}
	
	public ItemStack tapWith(List<ItemStack> stacks, float thickness, long secondsFermented, float downfall) {
		float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
		double alcPercent = getAlcPercent(this.fermentationData.fermentationSpeedMod(), thickness, downfall, ageIngameDays);
		if (alcPercent >= 100) {
			return SpectrumItems.PURE_ALCOHOL.getDefaultInstance();
		} else {
			// add up all stew effects with their durations from the input stacks
			var stewEffects = new HashMap<Holder<MobEffect>, Double>();
			for (var stack : stacks) {
				var stewEffectsComponent = SuspiciousStewEffects.EMPTY;
				if (stack.getItem() instanceof SuspiciousEffectHolder sussyBakka) // IN THIS WORL YOU ARE EITHER A SUSSY BAKKA OR A BUSSY SUKKA
					stewEffectsComponent = sussyBakka.getSuspiciousEffects();
				
				for (var effect : stewEffectsComponent.effects()) {
					var key = effect.effect();
					var duration = effect.duration() * (Support.logBase(2, 1 + stack.getCount()));
					stewEffects.put(key, stewEffects.getOrDefault(key, 0d) + duration);
				}
			}
			
			var finalStatusEffects = new ArrayList<MobEffectInstance>();
			double clampedAlcPercent = Mth.clamp(alcPercent, 1D, 20D); // a too high number will cause issues with the effects length exceeding the integer limit, lol
			for (var entry : stewEffects.entrySet()) {
				var finalDurationTicks = entry.getValue() * Math.pow(2, clampedAlcPercent);
				finalStatusEffects.add(new MobEffectInstance(entry.getKey(), (int) finalDurationTicks, 0));
			}
			
			ItemStack outputStack = OUTPUT_STACK.copy();
			outputStack.setCount(1);
			outputStack.set(SpectrumDataComponentTypes.BEVERAGE, new BeverageComponent((long) ageIngameDays, (int) alcPercent, thickness));
			outputStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.empty(), finalStatusEffects));
			return outputStack;
		}
	}
	
	@Override
	public boolean matches(StorageRecipeInput<SingleVariantStorage<FluidVariant>> recipeInput, Level world) {
		boolean flowerFound = false;
		for (int i = 0; i < recipeInput.size(); i++) {
			ItemStack stack = recipeInput.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof FlowerBlock) {
					flowerFound = true;
				} else {
					return false;
				}
			}
		}
		
		return flowerFound;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.TITRATION_BARREL_SUSPICIOUS_BREW;
	}
	
}
