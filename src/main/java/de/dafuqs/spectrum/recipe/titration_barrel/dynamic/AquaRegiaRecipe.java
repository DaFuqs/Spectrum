package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.titration_barrel.*;
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

public class AquaRegiaRecipe extends SweetenableTitrationBarrelRecipe {

	public static final RecipeSerializer<AquaRegiaRecipe> SERIALIZER = new EmptyRecipeSerializer<>(AquaRegiaRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("hidden/collect_cookbooks/imbrifer_cookbook");
	public static final int MIN_FERMENTATION_TIME_HOURS = 24;
	public static final ItemStack OUTPUT_STACK = getDefaultStackWithCount(SpectrumItems.AQUA_REGIA, 4);
	public static final Item TAPPING_ITEM = Items.GLASS_BOTTLE;
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.ofItems(1, SpectrumBlocks.JADEITE_LOTUS_BULB));
		add(IngredientStack.ofItems(3, SpectrumItems.JADEITE_PETALS));
	}};

	public AquaRegiaRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER, INGREDIENT_STACKS, FluidIngredient.of(Fluids.WATER), OUTPUT_STACK, TAPPING_ITEM, MIN_FERMENTATION_TIME_HOURS, new FermentationData(0.2F, 0.01F, List.of()));
	}
	
	@Override
	public ItemStack tap(Inventory inventory, long secondsFermented, float downfall) {
		int bulbCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumBlocks.JADEITE_LOTUS_BULB.asItem());
		int petalCount = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.JADEITE_PETALS);
		boolean nectar = InventoryHelper.getItemCountInInventory(inventory, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
		
		float thickness = getThickness(bulbCount, petalCount);
		return tapWith(bulbCount, petalCount, nectar, thickness, secondsFermented, downfall);
	}
	
	@Override
	protected @NotNull List<StatusEffectInstance> getEffects(boolean nectar, double bloominess, double alcPercent) {
		List<StatusEffectInstance> effects = new ArrayList<>();

		int effectDuration = 1800;
		if (alcPercent >= 40) {
			effectDuration *= 2;
			effects.add(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, effectDuration, 3));
			effectDuration *= 1.5;
		}
		if (alcPercent >= 35) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.EFFECT_PROLONGING, effectDuration, (int) (alcPercent / 12)));
			effectDuration *= 2;
		}
		if (alcPercent >= 30) {
			effects.add(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, effectDuration));
			effectDuration *= 3;
		}
		if (alcPercent >= 20) {
			effects.add(new StatusEffectInstance(StatusEffects.ABSORPTION, effectDuration, (int) (alcPercent / 10)));
			effectDuration *= 2;
		}
		if (alcPercent >= 10) {
			effects.add(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, effectDuration));
			effectDuration *= 2;
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
	public boolean matches(TitrationBarrelBlockEntity inventory, World world) {
		boolean bulbsFound = false;
		
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.isOf(SpectrumBlocks.JADEITE_LOTUS_BULB.asItem())) {
				bulbsFound = true;
			} else if (!stack.isOf(SpectrumItems.JADEITE_PETALS) && !stack.isOf(SpectrumItems.MOONSTRUCK_NECTAR)) {
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
