package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.matchbooks.recipe.matchbook.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.effect.*;
import net.minecraft.fluid.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NecteredViognierRecipe extends SweetenableTitrationBarrelRecipe {

	public static final RecipeSerializer<NecteredViognierRecipe> SERIALIZER = new EmptyRecipeSerializer<>(NecteredViognierRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("hidden/collect_cookbooks/imperial_cookbook");

	public static final int MIN_FERMENTATION_TIME_HOURS = 24;
	public static final ItemStack OUTPUT_STACK = getDefaultStackWithCount(SpectrumItems.NECTERED_VIOGNIER, 4);
	public static final Item TAPPING_ITEM = Items.GLASS_BOTTLE;
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.NEPHRITE_BLOSSOM_BULB)));
		add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.GLASS_PEACH), Matchbook.empty(), null, 4));
	}};

	public NecteredViognierRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER, INGREDIENT_STACKS, FluidIngredient.of(Fluids.WATER), OUTPUT_STACK, TAPPING_ITEM, MIN_FERMENTATION_TIME_HOURS, new FermentationData(0.15F, 0.01F, List.of()));
	}
	
	@Override
	public ItemStack tap(Inventory inventory, long secondsFermented, float downfall) {
		int bulbCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.NEPHRITE_BLOSSOM_BULB);
		int petalCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.GLASS_PEACH);
		boolean nectar = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
		
		float thickness = getThickness(bulbCount, petalCount);
		return tapWith(bulbCount, petalCount, nectar, thickness, secondsFermented, downfall);
	}
	
	@Override
	protected @NotNull List<StatusEffectInstance> getEffects(boolean nectar, double bloominess, double alcPercent) {
		List<StatusEffectInstance> effects = new ArrayList<>();

		int effectDuration = (int) (150 * Math.round(alcPercent % 10));
		if (alcPercent >= 35) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.MAGIC_ANNULATION, effectDuration, (int) (alcPercent / 10)));
		}
		if (alcPercent >= 35) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, effectDuration, (int) (alcPercent / 10)));
			effectDuration *= 1.5;
		}
		if (alcPercent >= 30) {
			effects.add(new StatusEffectInstance(StatusEffects.STRENGTH, effectDuration, (int) (alcPercent / 10)));
			effectDuration *= 1.5;
		}
		if (alcPercent >= 20) {
			effects.add(new StatusEffectInstance(StatusEffects.RESISTANCE, effectDuration, (int) (alcPercent / 45)));
			effectDuration *= 1.5;
		}
		if (alcPercent >= 10) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, effectDuration));
			effectDuration *= 1.5;
		}
		if (nectar) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, effectDuration / 2));
		}

		int nectarMod = nectar ? 3 : 1;
		effectDuration = 1200;
		int alcAfterBloominess = (int) (alcPercent / (nectarMod + bloominess));
		if (alcAfterBloominess >= 40) {
			effects.add(new StatusEffectInstance(StatusEffects.BLINDNESS, effectDuration));
			effectDuration *= 2;
		}
		if (alcAfterBloominess >= 30) {
			effects.add(new StatusEffectInstance(StatusEffects.POISON, effectDuration));
			effectDuration *= 2;
		}
		if (alcAfterBloominess >= 20) {
			effects.add(new StatusEffectInstance(StatusEffects.NAUSEA, effectDuration));
			effectDuration *= 2;
		}
		if (alcAfterBloominess >= 10) {
			effects.add(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration));
		}
		return effects;
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		boolean bulbsFound = false;
		
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.isOf(SpectrumItems.NEPHRITE_BLOSSOM_BULB)) {
				bulbsFound = true;
			} else if (!stack.isOf(SpectrumItems.GLASS_PEACH) && !stack.isOf(SpectrumItems.MOONSTRUCK_NECTAR)) {
				return false;
			}
		}
		
		return bulbsFound;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
