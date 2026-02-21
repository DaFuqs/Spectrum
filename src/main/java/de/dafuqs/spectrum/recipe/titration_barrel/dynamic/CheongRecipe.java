package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.fluids.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CheongRecipe extends TitrationBarrelRecipe {
	
	public static final Item TAPPING_ITEM = Items.GLASS_BOTTLE;
	public static final int MIN_FERMENTATION_TIME_HOURS = 4;
	public static final ItemStack OUTPUT_STACK = getDefaultStackWithCount(SpectrumItems.CHEONG.get(), 4);
	public static final ItemStack OUTPUT_STACK_MERMAIDS = getDefaultStackWithCount(SpectrumItems.MERMAIDS_JAM.get(), 4);
	
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.ofTag(SpectrumItemTags.FRUITS, 8));
		add(IngredientStack.ofItems(Items.SUGAR, 16));
	}};
	
	public CheongRecipe() {
		super("", false, Optional.empty(), INGREDIENT_STACKS, FluidIngredient.of(Fluids.WATER),
				OUTPUT_STACK, TAPPING_ITEM, MIN_FERMENTATION_TIME_HOURS, Optional.empty());
	}
	
	@Override
	public ItemStack tap(Container inventory, long secondsFermented, float downfall) {
		ItemStack result = inventory.hasAnyOf(Collections.singleton(SpectrumItems.MERMAIDS_GEM.get()))
				? OUTPUT_STACK_MERMAIDS.copy()
				: OUTPUT_STACK.copy();
		result.setCount(1);
		return result;
	}
	
	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.TITRATION_BARREL_CHEONG;
	}
	
}
