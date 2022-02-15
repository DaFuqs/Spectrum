package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.azure_dike.AzureDikeComponent;
import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.Optional;

public interface AzureDikeItem {
	
	int maxAzureDike();
	
	float azureDikeChargeBonus();
	
	default void recalculate(LivingEntity livingEntity) {
		if(!livingEntity.getWorld().isClient) {
			AzureDikeComponent azureDikeComponent = AzureDikeProvider.AZURE_DIKE_COMPONENT.get(livingEntity);
			
			Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(livingEntity);
			if (trinketComponent.isPresent()) {
				int maxProtection = 0;
				float rechargeRate = 0;
				for (Pair<SlotReference, ItemStack> pair : trinketComponent.get().getAllEquipped()) {
					if (pair.getRight().getItem() instanceof AzureDikeItem azureDikeItem) {
						maxProtection += azureDikeItem.maxAzureDike();
						rechargeRate += azureDikeItem.azureDikeChargeBonus();
					}
				}
				azureDikeComponent.set(maxProtection, rechargeRate, false);
			}
		}
	}
	
}
