package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.*;

import java.util.*;

public interface SlotReservingItem {
	
	static boolean isReservingSlot(ItemStack stack) {
		return stack.has(SpectrumDataComponentTypes.SLOT_RESERVER);
	}
	
	static @Nullable UUID getReserver(ItemStack stack) {
		return stack.get(SpectrumDataComponentTypes.SLOT_RESERVER);
	}
	
	static boolean isReserver(ItemStack stack,  UUID uuid) {
		return uuid.equals(getReserver(stack));
	}
	
	static void reserve(ItemStack stack, UUID reserver) {
		stack.set(SpectrumDataComponentTypes.SLOT_RESERVER, reserver);
	}
	
	static void free(ItemStack stack) {
		stack.remove(SpectrumDataComponentTypes.SLOT_RESERVER);
	}
	
	static void inventoryTick(Entity entity, ItemStack stack) {
		if (entity instanceof Player player) {
			if (player.getCooldowns().isOnCooldown(stack.getItem()) || SlotReservingItem.isReservingSlot(stack)) {
				stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
			} else {
				stack.set(DataComponents.ATTRIBUTE_MODIFIERS, stack.getPrototype().get(DataComponents.ATTRIBUTE_MODIFIERS));
			}
		}
	}
	
}
