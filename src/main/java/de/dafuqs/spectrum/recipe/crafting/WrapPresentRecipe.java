package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class WrapPresentRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<WrapPresentRecipe> SERIALIZER = new SpecialRecipeSerializer<>(WrapPresentRecipe::new);
	
	public WrapPresentRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public ItemStack getOutput() {
		return SpectrumBlocks.PRESENT.asItem().getDefaultStack();
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> list = DefaultedList.ofSize(1, Ingredient.EMPTY);
		ItemStack stack = SpectrumBlocks.PRESENT.asItem().getDefaultStack();
		PresentItem.wrap(stack, PresentBlock.Variant.RED, Map.of());
		list.set(0, Ingredient.ofStacks(stack));
		return list;
	}
	
	@Override
	public boolean matches(@NotNull CraftingInventory craftingInventory, World world) {
		boolean presentItemFound = false;
		boolean wrappingItemFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof PresentItem) {
					if (presentItemFound || PresentItem.isWrapped(itemStack)) {
						return false;
					}
					presentItemFound = true;
				} else if (!wrappingItemFound && getPresentVariantForStack(itemStack) != null) {
					wrappingItemFound = true;
				} else if (!(itemStack.getItem() instanceof PigmentItem)) {
					return false;
				}
			}
		}
		
		return presentItemFound;
	}
	
	@Override
	public ItemStack craft(@NotNull CraftingInventory craftingInventory) {
		ItemStack presentStack = ItemStack.EMPTY;
		PresentBlock.Variant variant = PresentBlock.Variant.RED;
		Map<DyeColor, Integer> colors = new HashMap<>();
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack stack = craftingInventory.getStack(j);
			if (stack.getItem() instanceof PresentItem) {
				presentStack = stack.copy();
				PresentItem.wrap(presentStack, variant, colors);
			} else if (stack.getItem() instanceof PigmentItem pigmentItem) {
				DyeColor color = pigmentItem.getColor();
				if (colors.containsKey(color)) {
					colors.put(color, colors.get(color) + 1);
				} else {
					colors.put(color, 1);
				}
			}
			PresentBlock.Variant stackVariant = getPresentVariantForStack(stack);
			if (stackVariant != null) {
				variant = stackVariant;
			}
		}
		
		return presentStack;
	}
	
	public @Nullable PresentBlock.Variant getPresentVariantForStack(@NotNull ItemStack stack) {
		Item item = stack.getItem();
		if (item == Items.RED_DYE) {
			return PresentBlock.Variant.RED;
		} else if (item == Items.BLUE_DYE) {
			return PresentBlock.Variant.BLUE;
		} else if (item == Items.CYAN_DYE) {
			return PresentBlock.Variant.CYAN;
		} else if (item == Items.GREEN_DYE) {
			return PresentBlock.Variant.GREEN;
		} else if (item == Items.PURPLE_DYE) {
			return PresentBlock.Variant.PURPLE;
		} else if (item == Items.CAKE) {
			return PresentBlock.Variant.CAKE;
		} else if (stack.isIn(ItemTags.FLOWERS)) {
			return PresentBlock.Variant.STRIPED;
		} else if (item == Items.FIREWORK_STAR) {
			return PresentBlock.Variant.STARRY;
		} else if (stack.isIn(ItemTags.SAPLINGS)) {
			return PresentBlock.Variant.WINTER;
		} else if (item == Items.SPORE_BLOSSOM) {
			return PresentBlock.Variant.PRIDE;
		}
		return null;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width * height >= 1;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
