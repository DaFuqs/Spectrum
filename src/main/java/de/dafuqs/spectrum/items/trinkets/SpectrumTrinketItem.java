package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.progression.*;
import dev.emi.trinkets.api.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

import java.util.*;

public abstract class SpectrumTrinketItem extends Item implements ICurioItem {
	
	private final ResourceLocation unlockIdentifier;
	
	public SpectrumTrinketItem(Properties settings, ResourceLocation unlockIdentifier) {
		super(settings);
		this.unlockIdentifier = unlockIdentifier;
	}
	
	public static boolean hasEquipped(SlotContext slotContext, Item item) {
		return hasEquipped(slotContext.entity(), item);
	}
	
	public static boolean hasEquipped(LivingEntity entity, Item item) {
		return getFirstEquipped(entity, item).isPresent();
	}
	
	public static Optional<ItemStack> getFirstEquipped(LivingEntity entity, Item item) {
		return CuriosApi.getCuriosInventory(entity).flatMap(inventory -> inventory.findFirstCurio(item)).map(SlotResult::stack);
	}
	
	public ResourceLocation getUnlockIdentifier() {
		return this.unlockIdentifier;
	}
	
	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack) {
		if (slotContext.entity() instanceof Player playerEntity) {
			// does the player have the matching advancement?
			if (AdvancementHelper.hasAdvancement(playerEntity, getUnlockIdentifier())) {
				// Can only a single trinket of that type be equipped at once?
				if (!canEquipMoreThanOne() && hasEquipped(slotContext.entity(), this)) {
					return false;
				}
				return ICurioItem.super.canEquip(slotContext, stack);
			}
		}
		return false;
	}
	
	public boolean canEquipMoreThanOne() {
		return false;
	}
	
	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		ICurioItem.super.onEquip(slotContext, prevStack, stack);
		if (slotContext.entity() instanceof ServerPlayer serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		ICurioItem.super.onUnequip(slotContext, newStack, stack);
		
		if (slotContext.entity() instanceof ServerPlayer serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
	@Override
	public void curioBreak(SlotContext slotContext, ItemStack stack) {
		ICurioItem.super.curioBreak(slotContext, stack);
		
		if (slotContext.entity() instanceof ServerPlayer serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
}
