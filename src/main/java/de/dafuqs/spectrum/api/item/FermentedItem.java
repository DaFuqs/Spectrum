package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

public interface FermentedItem {
	
	static boolean isPreviewStack(ItemStack stack) {
		return stack.has(SpectrumDataComponentTypes.IS_PREVIEW_ITEM);
	}
	
	static void setPreviewStack(ItemStack stack) {
		stack.set(SpectrumDataComponentTypes.IS_PREVIEW_ITEM, Unit.INSTANCE);
	}
	
}
