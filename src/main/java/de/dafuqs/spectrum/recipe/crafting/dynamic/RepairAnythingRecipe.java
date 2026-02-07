package de.dafuqs.spectrum.recipe.crafting.dynamic;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class RepairAnythingRecipe extends CustomRecipe {
	
	public RepairAnythingRecipe() {
		super(CraftingBookCategory.MISC);
	}
	
	@Override
	public boolean matches(CraftingInput input, Level world) {
		boolean nectarFound = false;
		boolean itemFound = false;
		
		for (int j = 0; j < input.size(); ++j) {
			ItemStack itemStack = input.getItem(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.is(SpectrumItems.MOONSTRUCK_NECTAR)) {
					if (nectarFound) {
						return false;
					}
					nectarFound = true;
				} else if (itemStack.isDamageableItem() && itemStack.isDamaged() && !itemStack.is(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
					if (itemFound) {
						return false;
					}
					itemFound = true;
				}
			}
		}
		
		return nectarFound && itemFound;
	}
	
	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryLookup) {
		ItemStack itemStack = ItemStack.EMPTY;
		for (int j = 0; j < input.size(); ++j) {
			itemStack = input.getItem(j);
			if (!itemStack.isEmpty() && !itemStack.is(SpectrumItems.MOONSTRUCK_NECTAR)) {
				break;
			}
		}
		
		if (itemStack.isDamageableItem() && itemStack.isDamaged() && !itemStack.is(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
			ItemStack returnStack = itemStack.copy();
			int damage = returnStack.getDamageValue();
			int maxDamage = returnStack.getMaxDamage();
			
			int newDamage = Math.max(0, damage - maxDamage / 3);
			returnStack.setDamageValue(newDamage);
			return returnStack;
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.REPAIR_ANYTHING_SERIALIZER;
	}
	
}
