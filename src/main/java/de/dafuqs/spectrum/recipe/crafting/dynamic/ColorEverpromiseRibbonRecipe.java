package de.dafuqs.spectrum.recipe.crafting.dynamic;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class ColorEverpromiseRibbonRecipe extends CustomRecipe {
	
	public ColorEverpromiseRibbonRecipe() {
		super(CraftingBookCategory.MISC);
	}
	
	@Override
	public boolean matches(CraftingInput input, Level world) {
		boolean ribbonFound = false;
		boolean pigmentFound = false;
		
		for (int i = 0; i < input.size(); ++i) {
			ItemStack itemStack = input.getItem(i);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof EverpromiseRibbonItem) {
					if (!itemStack.has(DataComponents.CUSTOM_NAME)) {
						return false;
					}
					if (ribbonFound) {
						return false;
					} else {
						ribbonFound = true;
					}
				} else if (itemStack.getItem() instanceof PigmentItem) {
					if (pigmentFound) {
						return false;
					} else {
						pigmentFound = true;
					}
				} else {
					return false;
				}
			}
		}
		
		return ribbonFound && pigmentFound;
	}
	
	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryLookup) {
		ItemStack ribbon = null;
		PigmentItem pigment = null;
		
		
		for (int i = 0; i < input.size(); ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.getItem() instanceof EverpromiseRibbonItem) {
				ribbon = stack;
			}
			if (stack.getItem() instanceof PigmentItem pigmentItem) {
				pigment = pigmentItem;
			}
		}
		
		if (ribbon == null || pigment == null) {
			return ItemStack.EMPTY;
		}
		
		ribbon = ribbon.copy();
		ribbon.setCount(1);
		
		Component text = ribbon.getHoverName();
		if (text instanceof MutableComponent mutableText) {
			TextColor newColor = TextColor.fromRgb(pigment.getInkColor().getColorInt());
			Component newName = mutableText.setStyle(mutableText.getStyle().withColor(newColor));
			ribbon.set(DataComponents.CUSTOM_NAME, newName);
		}
		
		return ribbon;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.COLOR_EVERPROMISE_RIBBON_SERIALIZER;
	}
	
}
