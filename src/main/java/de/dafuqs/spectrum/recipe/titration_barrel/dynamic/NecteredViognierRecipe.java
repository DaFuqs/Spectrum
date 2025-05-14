package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.resources.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NecteredViognierRecipe extends SweetenableTitrationBarrelRecipe {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("hidden/collect_cookbooks/imperial_cookbook");
	
	public static final int MIN_FERMENTATION_TIME_HOURS = 24;
	public static final ItemStack OUTPUT_STACK = getDefaultStackWithCount(SpectrumItems.NECTERED_VIOGNIER, 4);
	public static final Item TAPPING_ITEM = Items.GLASS_BOTTLE;
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.ofItems(SpectrumBlocks.NEPHRITE_BLOSSOM_BULB.asItem()));
		add(IngredientStack.ofItems(SpectrumItems.GLASS_PEACH, 4));
	}};
	
	public NecteredViognierRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER), INGREDIENT_STACKS, FluidIngredient.of(Fluids.WATER), OUTPUT_STACK, TAPPING_ITEM, MIN_FERMENTATION_TIME_HOURS, new FermentationData(0.15F, 0.01F, List.of()));
	}
	
	@Override
	public ItemStack tap(Container inventory, long secondsFermented, float downfall) {
		int bulbCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumBlocks.NEPHRITE_BLOSSOM_BULB.asItem());
		int petalCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.GLASS_PEACH);
		boolean nectar = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
		
		float thickness = getThickness(bulbCount, petalCount);
		return tapWith(bulbCount, petalCount, nectar, thickness, secondsFermented, downfall);
	}
	
	@Override
	protected @NotNull List<MobEffectInstance> getEffects(boolean nectar, double bloominess, double alcPercent) {
		List<MobEffectInstance> effects = new ArrayList<>();
		
		//TODO should this be a float, and only casted to int at the end?
		int effectDuration = (int) (150 * Math.round(alcPercent / 10));
		if (alcPercent >= 35) {
			effects.add(new MobEffectInstance(SpectrumStatusEffects.MAGIC_ANNULATION, effectDuration, (int) (alcPercent / 10)));
		}
		if (alcPercent >= 35) {
			effects.add(new MobEffectInstance(SpectrumStatusEffects.TOUGHNESS, effectDuration, (int) (alcPercent / 10)));
			effectDuration *= 1.5;
		}
		if (alcPercent >= 30) {
			effects.add(new MobEffectInstance(MobEffects.DAMAGE_BOOST, effectDuration, (int) (alcPercent / 10)));
			effectDuration *= 1.5;
		}
		if (alcPercent >= 20) {
			effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, effectDuration, (int) (alcPercent / 45)));
			effectDuration *= 1.5;
		}
		if (alcPercent >= 10) {
			effects.add(new MobEffectInstance(SpectrumStatusEffects.NOURISHING, effectDuration));
			effectDuration *= 1.5;
		}
		if (nectar) {
			effects.add(new MobEffectInstance(SpectrumStatusEffects.IMMUNITY, effectDuration / 2));
		}
		
		int nectarMod = nectar ? 3 : 1;
		effectDuration = 1200;
		int alcAfterBloominess = (int) (alcPercent / (nectarMod + bloominess));
		if (alcAfterBloominess >= 40) {
			effects.add(new MobEffectInstance(MobEffects.BLINDNESS, effectDuration));
			effectDuration *= 2;
		}
		if (alcAfterBloominess >= 30) {
			effects.add(new MobEffectInstance(MobEffects.POISON, effectDuration));
			effectDuration *= 2;
		}
		if (alcAfterBloominess >= 20) {
			effects.add(new MobEffectInstance(MobEffects.CONFUSION, effectDuration));
			effectDuration *= 2;
		}
		if (alcAfterBloominess >= 10) {
			effects.add(new MobEffectInstance(MobEffects.WEAKNESS, effectDuration));
		}
		return effects;
	}
	
	@Override
	public boolean matches(StorageRecipeInput<SingleVariantStorage<FluidVariant>> recipeInput, Level world) {
		boolean bulbsFound = false;
		
		for (int i = 0; i < recipeInput.size(); i++) {
			ItemStack stack = recipeInput.getItem(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.is(SpectrumBlocks.NEPHRITE_BLOSSOM_BULB.asItem())) {
				bulbsFound = true;
			} else if (!stack.is(SpectrumItems.GLASS_PEACH) && !stack.is(SpectrumItems.MOONSTRUCK_NECTAR)) {
				return false;
			}
		}
		
		return bulbsFound;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.TITRATION_BARREL_NECTERED_VIOGNIER;
	}
	
}
