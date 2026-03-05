package de.dafuqs.spectrum.api.item;

import net.minecraft.world.item.*;

public interface AzureDikeItem {
	
	int maxAzureDike(ItemStack stack);
	
	default float azureDikeRechargeSpeedModifier(ItemStack stack) {
		return 1.0F;
	}
	
	default float rechargeDelayAfterDamageModifier(ItemStack stack) {
		return 1.0F;
	}
	
	default float maxAzureDikeMultiplier(ItemStack stack) {
		return 1.0F;
	}
	
}
