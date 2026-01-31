package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

import java.util.*;

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
	
	default void recalculate(LivingEntity livingEntity) {
		Level world = livingEntity.level();
		if (!world.isClientSide) {
			AzureDikeAttachmentType azureDikeComponent = livingEntity.getData(AzureDikeAttachmentType.ATTACHMENT_TYPE);
			
			Optional<ICuriosItemHandler> trinketComponent = CuriosApi.getCuriosInventory(livingEntity);
			if (trinketComponent.isPresent()) {
				int maxAzureDike = 0;
				float rechargeSpeedModifier = 1F;
				float rechargeDelayAfterDamageModifier = 1F;
				float maxAzureDikeMultiplier = 1F;
				for (SlotResult slot : trinketComponent.get().findCurios(stack -> stack.getItem() instanceof AzureDikeItem)) {
					ItemStack stack = slot.stack();
					AzureDikeItem azureDikeItem = (AzureDikeItem) stack.getItem();
					maxAzureDike += azureDikeItem.maxAzureDike(stack);
					rechargeSpeedModifier += azureDikeItem.azureDikeRechargeSpeedModifier(stack) - 1;
					rechargeDelayAfterDamageModifier += azureDikeItem.rechargeDelayAfterDamageModifier(stack) - 1;
					maxAzureDikeMultiplier += azureDikeItem.maxAzureDikeMultiplier(stack) - 1;
				}
				
				int ticksPerPointOfRecharge = (int) Math.max(1, AzureDikeAttachmentType.BASE_RECHARGE_DELAY_TICKS / rechargeSpeedModifier);
				int rechargeDelayTicksAfterGettingHit = (int) Math.max(1, AzureDikeAttachmentType.BASE_RECHARGE_DELAY_TICKS_AFTER_DAMAGE / rechargeDelayAfterDamageModifier);
				
				azureDikeComponent.set(Math.round((maxAzureDike * maxAzureDikeMultiplier)), ticksPerPointOfRecharge, rechargeDelayTicksAfterGettingHit, false);
				azureDikeComponent.sync(livingEntity);
			}
		}
	}
	
}
