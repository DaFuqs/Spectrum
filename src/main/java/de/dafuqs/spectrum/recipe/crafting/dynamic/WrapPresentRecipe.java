package de.dafuqs.spectrum.recipe.crafting.dynamic;

import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class WrapPresentRecipe extends CustomRecipe {
	
	public WrapPresentRecipe() {
		super(CraftingBookCategory.MISC);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.withSize(1, Ingredient.EMPTY);
		list.set(0, Ingredient.of(SpectrumBlocks.PRESENT.asItem().getDefaultInstance()));
		return list;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryLookup) {
		ItemStack stack = SpectrumBlocks.PRESENT.asItem().getDefaultInstance();
		PresentBlockItem.wrap(stack, PresentBlock.WrappingPaper.RED, Map.of());
		return stack;
	}
	
	@Override
	public boolean matches(@NotNull CraftingInput input, Level world) {
		boolean presentItemFound = false;
		boolean wrappingItemFound = false;
		
		for (int j = 0; j < input.size(); ++j) {
			ItemStack itemStack = input.getItem(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof PresentBlockItem) {
					if (presentItemFound || PresentBlockItem.isWrapped(itemStack)) {
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
	public ItemStack assemble(@NotNull CraftingInput input, HolderLookup.Provider registryLookup) {
		ItemStack presentStack = ItemStack.EMPTY;
		PresentBlock.WrappingPaper wrappingPaper = PresentBlock.WrappingPaper.RED;
		Map<Integer, Integer> colors = new HashMap<>();
		
		for (int j = 0; j < input.size(); ++j) {
			ItemStack stack = input.getItem(j);
			if (stack.getItem() instanceof PresentBlockItem) {
				presentStack = stack.copy();
			} else if (stack.getItem() instanceof PigmentItem pigmentItem) {
				int color = pigmentItem.getInkColor().getColorInt();
				if (colors.containsKey(color)) {
					colors.put(color, colors.get(color) + 1);
				} else {
					colors.put(color, 1);
				}
			}
			PresentBlock.WrappingPaper stackWrappingPaper = getPresentVariantForStack(stack);
			if (stackWrappingPaper != null) {
				wrappingPaper = stackWrappingPaper;
			}
		}
		
		if (!presentStack.isEmpty()) {
			PresentBlockItem.wrap(presentStack, wrappingPaper, colors);
		}
		return presentStack;
	}
	
	public @Nullable PresentBlock.WrappingPaper getPresentVariantForStack(@NotNull ItemStack stack) {
		Item item = stack.getItem();
		if (item == Items.RED_DYE) {
			return PresentBlock.WrappingPaper.RED;
		} else if (item == Items.BLUE_DYE) {
			return PresentBlock.WrappingPaper.BLUE;
		} else if (item == Items.CYAN_DYE) {
			return PresentBlock.WrappingPaper.CYAN;
		} else if (item == Items.GREEN_DYE) {
			return PresentBlock.WrappingPaper.GREEN;
		} else if (item == Items.PURPLE_DYE) {
			return PresentBlock.WrappingPaper.PURPLE;
		} else if (item == Items.CAKE) {
			return PresentBlock.WrappingPaper.CAKE;
		} else if (stack.is(ItemTags.FLOWERS)) {
			return PresentBlock.WrappingPaper.STRIPED;
		} else if (item == Items.FIREWORK_STAR) {
			return PresentBlock.WrappingPaper.STARRY;
		} else if (item == Items.SNOWBALL) {
			return PresentBlock.WrappingPaper.WINTER;
		} else if (item == Items.SPORE_BLOSSOM) {
			return PresentBlock.WrappingPaper.PRIDE;
		}
		return null;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 1;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.WRAP_PRESENT_SERIALIZER;
	}
	
}
