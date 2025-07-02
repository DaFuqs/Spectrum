package de.dafuqs.spectrum.compat.vanityslots;

import gay.nyako.vanityslots.*;
import net.fabricmc.loader.api.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

public class VanitySlotsCompat {
	public static final boolean IS_VANITY_SLOTS_PRESENT = FabricLoader.getInstance().isModLoaded("vanityslots");
	
	public static ItemStack getEquippedStack(LivingEntity entity, EquipmentSlot slot) {
		if (VanitySlotsCompat.IS_VANITY_SLOTS_PRESENT) {
			return CommonClass.getEquippedStack(entity, slot);
		} else {
			return entity.getItemBySlot(slot);
		}
	}
}
