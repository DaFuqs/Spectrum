package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

public interface ActivatableItem {
	
	static void setActivated(ItemStack stack, boolean activated) {
		if (activated)
			stack.set(SpectrumDataComponentTypes.ACTIVATED, Unit.INSTANCE);
		else
			stack.remove(SpectrumDataComponentTypes.ACTIVATED);
	}
	
	static boolean isActivated(ItemStack stack) {
		return stack.has(SpectrumDataComponentTypes.ACTIVATED);
	}
	
}
