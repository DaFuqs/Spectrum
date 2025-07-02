package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

import java.util.*;

public class AzureDikeAmuletItem extends InkDrainTrinketItem implements AzureDikeItem {
	
	public AzureDikeAmuletItem(Properties settings, ResourceLocation unlockIdentifier) {
		super(settings, unlockIdentifier, InkColors.BLUE);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.azure_dike_provider.tooltip", maxAzureDike(stack)));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		recalculate(entity);
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onUnequip(stack, slot, entity);
		recalculate(entity);
	}
	
	@Override
	public void onBreak(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onBreak(stack, slot, entity);
		recalculate(entity);
	}
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		if (storedInk < 100) {
			return 0;
		} else {
			return getDike(storedInk);
		}
	}
	
	public int getDike(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 2 + 2 * (int) (Math.log(storedInk / 100.0f) / Math.log(8));
		}
	}
	
}
