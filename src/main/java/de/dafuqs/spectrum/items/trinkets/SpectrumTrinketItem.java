package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
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
	
	private final Identifier unlockIdentifier;
	
	public SpectrumTrinketItem(Settings settings, Identifier unlockIdentifier) {
		super(settings);
		this.unlockIdentifier = unlockIdentifier;
	}
	
	public static boolean hasEquipped(Object entity, Item item) {
		if (entity instanceof LivingEntity livingEntity) {
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
	
	protected Identifier getUnlockIdentifier() {
		return this.unlockIdentifier;
	}
	
	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof PlayerEntity playerEntity) {
			// does the player have the matching advancement?
			if (Support.hasAdvancement(playerEntity, getUnlockIdentifier())) {
				// Can only a single trinket of that type be equipped at once?
				if (!canEquipMoreThanOne() && hasEquipped(entity, this)) {
					return false;
				}
				return super.canEquip(stack, slot, entity);
			}
		}
		return false;
	}
	
	public boolean canEquipMoreThanOne() {
		return false;
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onUnequip(stack, slot, entity);
		if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
	@Override
	public void onBreak(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onBreak(stack, slot, entity);
		if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
	}
	
}
