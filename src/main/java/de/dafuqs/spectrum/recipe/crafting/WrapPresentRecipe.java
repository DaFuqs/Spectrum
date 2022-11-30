package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.blocks.present.PresentBlock;
import de.dafuqs.spectrum.blocks.present.PresentItem;
import de.dafuqs.spectrum.items.PigmentItem;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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
		DefaultedList<Ingredient> l = DefaultedList.ofSize(1, Ingredient.EMPTY);
		ItemStack stack = SpectrumBlocks.PRESENT.asItem().getDefaultStack();
		PresentItem.wrap(stack, PresentBlock.Variant.RED, Map.of());
		l.set(0, Ingredient.ofStacks(stack));
		return l;
	}
	
	public boolean matches(CraftingInventory craftingInventory, World world) {
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
				} else if (!wrappingItemFound && getVariantForStack(itemStack) != null) {
					wrappingItemFound = true;
				} else if (!(itemStack.getItem() instanceof PigmentItem)) {
					return false;
				}
			}
		}
		
		return presentItemFound;
	}
	
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack presentStack = ItemStack.EMPTY;
		PresentBlock.Variant variant = PresentBlock.Variant.RED;
		Map<DyeColor, Integer> colors = new HashMap<>();
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack stack = craftingInventory.getStack(j);
			if (stack.getItem() instanceof PresentItem) {
				presentStack = stack.copy();
			} else if (stack.getItem() instanceof PigmentItem pigmentItem) {
				DyeColor color = pigmentItem.getColor();
				if (colors.containsKey(color)) {
					colors.put(color, colors.get(color) + 1);
				} else {
					colors.put(color, 1);
				}
			}
			PresentBlock.Variant stackVariant = getVariantForStack(stack);
			if (stackVariant != null) {
				variant = stackVariant;
			}
		}
		
		PresentItem.wrap(presentStack, variant, colors);
		return presentStack;
	}
	
	public @Nullable PresentBlock.Variant getVariantForStack(ItemStack stack) {
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
	
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
