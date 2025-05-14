package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.item.*;

public interface ActivatableItem {
	
	static void setActivated(ItemStack stack, boolean activated) {
		ComponentHelper.setOrRemove(stack, SpectrumDataComponentTypes.ACTIVATED, activated);
	}
	
	static boolean isActivated(ItemStack stack) {
		return stack.has(SpectrumDataComponentTypes.ACTIVATED);
	}
	
}
