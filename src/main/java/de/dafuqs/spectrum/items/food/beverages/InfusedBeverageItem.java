package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import net.minecraft.item.*;
import net.minecraft.util.collection.*;

public class InfusedBeverageItem extends BeverageItem {
	
	public InfusedBeverageItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return VariantBeverageProperties.getFromStack(itemStack);
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		
		// adding all beverages from recipes
		if (this.isIn(group) && SpectrumCommon.minecraftServer != null) {
			for (ITitrationBarrelRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.TITRATION_BARREL)) {
				ItemStack output = recipe.getOutput().copy();
				if (output.getItem() instanceof InfusedBeverageItem) {
					output.setCount(1);
					stacks.add(output);
				}
			}
		}
	}
	
}
