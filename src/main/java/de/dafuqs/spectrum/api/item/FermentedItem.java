package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.items.food.beverages.properties.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;

public interface FermentedItem {
	
	BeverageProperties getBeverageProperties(ItemStack itemStack);
	
	static boolean isPreviewStack(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		return nbtCompound != null && nbtCompound.getBoolean("Preview");
	}
	
	static void setPreviewStack(ItemStack stack) {
		NbtCompound compound = stack.getOrCreateNbt();
		compound.putBoolean("Preview", true);
		stack.setNbt(compound);
	}
	
}
