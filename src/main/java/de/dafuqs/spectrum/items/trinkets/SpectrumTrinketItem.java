package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Optional;

public abstract class SpectrumTrinketItem extends TrinketItem {
	
	public SpectrumTrinketItem(Settings settings) {
		super(settings);
	}
	
	protected abstract Identifier getUnlockIdentifier();
	
	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if(entity instanceof PlayerEntity playerEntity) {
			if(Support.hasAdvancement(playerEntity, getUnlockIdentifier())) {
				return super.canEquip(stack, slot, entity);
			}
		}
		return false;
	}
	
	public static boolean hasEquipped(Object entity, Item item) {
		if(entity instanceof LivingEntity livingEntity) {
			return hasEquipped(livingEntity, item);
		}
		return false;
	}
	
	public static boolean hasEquipped(LivingEntity entity, Item item) {
		Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(entity);
		return trinketComponent.map(component -> component.isEquipped(item)).orElse(false);
	}
	
	public static Optional<ItemStack> getFirstEquipped(LivingEntity entity, Item item) {
		Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(entity);
		if (trinketComponent.isPresent()) {
			List<Pair<SlotReference, ItemStack>> stacks = trinketComponent.get().getEquipped(item);
			if (!stacks.isEmpty()) {
				return Optional.of(stacks.get(0).getRight());
			}
		}
		return Optional.empty();
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		if(entity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onUnequip(stack, slot, entity);
		if(entity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
	@Override
	public void onBreak(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onBreak(stack, slot, entity);
		if(entity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
}
