package de.dafuqs.spectrum.compat.vanityslots;

import de.dafuqs.spectrum.compat.*;
import gay.nyako.vanityslots.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

public class VanitySlotsCompat {
	public static final boolean IS_VANITY_SLOTS_PRESENT = SpectrumIntegrationPacks.isModLoaded("vanityslots");
	
	public static ItemStack getEquippedStack(LivingEntity entity, EquipmentSlot slot) {
		// TODO: port
		/*if (VanitySlotsCompat.IS_VANITY_SLOTS_PRESENT) {
			return CommonClass.getEquippedStack(entity, slot);
		} else {
			return entity.getItemBySlot(slot);
		}*/
	}
}
