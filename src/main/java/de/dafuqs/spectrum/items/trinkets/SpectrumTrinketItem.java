package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.Support;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class SpectrumTrinketItem extends TrinketItem {
	
	public SpectrumTrinketItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if(entity instanceof PlayerEntity playerEntity) {
			if(Support.hasAdvancement(playerEntity, getUnlockIdentifier())) {
				return super.canEquip(stack, slot, entity);
			}
		}
		return false;
	}
	
	protected abstract Identifier getUnlockIdentifier();
	
	public static boolean hasEquipped(LivingEntity entity, Item item) {
		Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(entity);
		return trinketComponent.map(component -> component.isEquipped(item)).orElse(false);
	}
	
}
