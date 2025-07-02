package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface SlotReservingItem {
	
	static boolean isReservingSlot(ItemStack stack) {
		return stack.has(SpectrumDataComponentTypes.SLOT_RESERVER);
	}
	
	static UUID getReserver(ItemStack stack) {
		return stack.get(SpectrumDataComponentTypes.SLOT_RESERVER);
	}
	
	static boolean isReserver(ItemStack stack, @NotNull  UUID uuid) {
		return uuid.equals(getReserver(stack));
	}
	
	static void reserve(ItemStack stack, UUID reserver) {
		stack.set(SpectrumDataComponentTypes.SLOT_RESERVER, reserver);
	}
	
	static void free(ItemStack stack) {
		stack.remove(SpectrumDataComponentTypes.SLOT_RESERVER);
	}
	
}
