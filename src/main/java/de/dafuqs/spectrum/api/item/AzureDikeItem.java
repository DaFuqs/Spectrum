package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public interface AzureDikeItem {
	
	Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/create_refined_azurite");
	
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
	
	default void recalculate(LivingEntity livingEntity) {
		World world = livingEntity.getWorld();
		if (!world.isClient) {
			AzureDikeComponent azureDikeComponent = AzureDikeProvider.AZURE_DIKE_COMPONENT.get(livingEntity);
			
			Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(livingEntity);
			if (trinketComponent.isPresent()) {
				int maxAzureDike = 0;
				float rechargeSpeedModifier = 1F;
				float rechargeDelayAfterDamageModifier = 1F;
				float maxAzureDikeMultiplier = 1F;
				for (Pair<SlotReference, ItemStack> pair : trinketComponent.get().getAllEquipped()) {
					ItemStack stack = pair.getRight();
					if (pair.getRight().getItem() instanceof AzureDikeItem azureDikeItem) {
						maxAzureDike += azureDikeItem.maxAzureDike(stack);
						rechargeSpeedModifier += azureDikeItem.azureDikeRechargeSpeedModifier(stack) - 1;
						rechargeDelayAfterDamageModifier += azureDikeItem.rechargeDelayAfterDamageModifier(stack) - 1;
						maxAzureDikeMultiplier += azureDikeItem.maxAzureDikeMultiplier(stack) - 1;
					}
				}
				
				int ticksPerPointOfRecharge = (int) Math.max(1, DefaultAzureDikeComponent.BASE_RECHARGE_DELAY_TICKS / rechargeSpeedModifier);
				int rechargeDelayTicksAfterGettingHit = (int) Math.max(1, DefaultAzureDikeComponent.BASE_RECHARGE_DELAY_TICKS_AFTER_DAMAGE / rechargeDelayAfterDamageModifier);
				
				azureDikeComponent.set(Math.round((maxAzureDike * maxAzureDikeMultiplier)), ticksPerPointOfRecharge, rechargeDelayTicksAfterGettingHit, false);
			}
		}
	}
	
}
