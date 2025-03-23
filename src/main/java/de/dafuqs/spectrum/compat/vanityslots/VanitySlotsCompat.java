package de.dafuqs.spectrum.compat.vanityslots;

import gay.nyako.vanityslots.*;
import net.fabricmc.loader.api.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;

public class VanitySlotsCompat {
	public static final boolean IS_VANITY_SLOTS_PRESENT = FabricLoader.getInstance().isModLoaded("vanityslots");
	
	public static ItemStack getEquippedStack(LivingEntity entity, EquipmentSlot slot) {
		if (VanitySlotsCompat.IS_VANITY_SLOTS_PRESENT) {
			return VanitySlots.getEquippedStack(entity, slot);
		} else {
			return entity.getEquippedStack(slot);
		}
	}
}
