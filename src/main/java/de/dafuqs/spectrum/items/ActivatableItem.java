package de.dafuqs.spectrum.items;

import net.minecraft.item.*;
import net.minecraft.nbt.*;

public interface ActivatableItem {
	
	String NBT_STRING = "activated";
	
	static void setActivated(ItemStack stack, boolean activated) {
		NbtCompound compound = stack.getOrCreateNbt();
		compound.putBoolean(NBT_STRING, activated);
		stack.setNbt(compound);
	}
	
	static boolean isActivated(ItemStack stack) {
		NbtCompound compound = stack.getNbt();
		if (compound != null && compound.contains(NBT_STRING)) {
			return compound.getBoolean(NBT_STRING);
		}
		return false;
	}
	
}
