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

public class JadeWineRecipe extends SweetenableTitrationBarrelRecipe {
	
	public static final RecipeSerializer<JadeWineRecipe> SERIALIZER = new EmptyRecipeSerializer<>(JadeWineRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/food/jade_wine");
	public static final int MIN_FERMENTATION_TIME_HOURS = 24;
	public static final ItemStack OUTPUT_STACK = getDefaultStackWithCount(SpectrumItems.JADE_WINE, 4);
	public static final Item TAPPING_ITEM = Items.GLASS_BOTTLE;
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.GERMINATED_JADE_VINE_BULB)));
		add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.JADE_VINE_PETALS), Matchbook.empty(), null, 3));
	}};
	
	public JadeWineRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER, INGREDIENT_STACKS, FluidIngredient.of(Fluids.WATER), OUTPUT_STACK, TAPPING_ITEM, MIN_FERMENTATION_TIME_HOURS, new FermentationData(0.075F, 0.01F, List.of()));
	}
	
	@Override
	public ItemStack tap(Inventory inventory, long secondsFermented, float downfall) {
		int bulbCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.GERMINATED_JADE_VINE_BULB);
		int petalCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.JADE_VINE_PETALS);
		boolean nectar = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
		
		float thickness = getThickness(bulbCount, petalCount);
		return tapWith(bulbCount, petalCount, nectar, thickness, secondsFermented, downfall);
	}
	
	@Override
	protected @NotNull List<StatusEffectInstance> getEffects(boolean nectar, double bloominess, double alcPercent) {
		List<StatusEffectInstance> effects = new ArrayList<>();

		int effectDuration = 1200;
		if (alcPercent >= 80) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.PROJECTILE_REBOUND, effectDuration));
			effectDuration *= 2;
		}
		if (alcPercent >= 70) {
			effects.add(new StatusEffectInstance(StatusEffects.RESISTANCE, effectDuration));
			effectDuration *= 2;
		}
		if (alcPercent >= 60) {
			effects.add(new StatusEffectInstance(StatusEffects.HASTE, effectDuration));
			effectDuration *= 2;
		}
		if (alcPercent >= 40) {
			effects.add(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, effectDuration));
			effectDuration *= 2;
		}
		if (alcPercent >= 20) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, effectDuration));
			effectDuration *= 2;
		}
		if (nectar) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, effectDuration));
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
			if (stack.isOf(SpectrumItems.GERMINATED_JADE_VINE_BULB)) {
				bulbsFound = true;
			} else if (!stack.isOf(SpectrumItems.JADE_VINE_PETALS) && !stack.isOf(SpectrumItems.MOONSTRUCK_NECTAR)) {
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
