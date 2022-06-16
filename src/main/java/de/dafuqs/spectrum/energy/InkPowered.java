package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.helpers.Support;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Optional;

public interface InkPowered {
	
	Identifier REQUIRED_ADVANCEMENT = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_ink_use");
	
	/**
	 * The colors that the object requires for working.
	 * These are added as the player facing tooltip
	 **/
	List<InkColor> getUsedColors();
	
	/**
	 * The colors that the object requires for working.
	 * These are added as the player facing tooltip
	 **/
	default void addInkPoweredTooltip(List<Text> tooltip) {
		if(Support.hasAdvancement(MinecraftClient.getInstance().player, REQUIRED_ADVANCEMENT)) {
			for (InkColor color : getUsedColors()) {
				tooltip.add(new TranslatableText("spectrum.tooltip.ink_powered." + color.toString()).formatted(Formatting.GRAY));
			}
		}
	}
	
	default long tryDrainEnergy(ItemStack stack, InkColor color, long amount) {
		if (stack.getItem() instanceof InkStorageItem inkStorageItem) {
			InkStorage inkStorage = inkStorageItem.getEnergyStorage(stack);
			return inkStorage.drainEnergy(color, amount);
		} else {
			return 0;
		}
	}
	
	/**
	 * Searches an inventory for InkEnergyStorageItems and tries to drain the color energy.
	 * If enough could be drained returns true, else false.
	 * If not enough energy is available it will be drained as much as is available
	 * but return will still be false
	 **/
	default boolean tryPayCost(Inventory inventory, InkColor color, long amount) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack currentStack = inventory.getStack(i);
			if (!currentStack.isEmpty()) { // fast fail
				amount -= tryDrainEnergy(currentStack, color, amount);
				if (amount <= 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Searches the players Trinkets for energy storage first and inventory second
	 * for PigmentEnergyStorageItem and tries to drain the color energy.
	 * If enough could be drained returns true, else false.
	 * If not enough energy is available it will be drained as much as is available
	 * but return will still be false
	 * <p>
	 * Check Order:
	 * - Offhand
	 * - Trinket Slots
	 * - Inventory
	 **/
	default boolean tryPayCost(ServerPlayerEntity player, InkColor color, long amount) {
		if(!Support.hasAdvancement(player, REQUIRED_ADVANCEMENT)) {
			return false;
		}
		
		// offhand
		for (ItemStack itemStack : player.getInventory().offHand) {
			amount -= tryDrainEnergy(itemStack, color, amount);
			if (amount <= 0) {
				return true;
			}
		}
		
		// trinket slot
		Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(player);
		if (optionalTrinketComponent.isPresent()) {
			List<Pair<SlotReference, ItemStack>> trinketInkStorages = optionalTrinketComponent.get().getEquipped(itemStack -> itemStack.getItem() instanceof InkStorage);
			for (Pair<SlotReference, ItemStack> trinketEnergyStorageStack : trinketInkStorages) {
				amount -= tryDrainEnergy(trinketEnergyStorageStack.getRight(), color, amount);
				if (amount <= 0) {
					return true;
				}
			}
		}
		
		// inventory
		for (ItemStack itemStack : player.getInventory().main) {
			amount -= tryDrainEnergy(itemStack, color, amount);
			if (amount <= 0) {
				return true;
			}
		}
		
		return false;
	}
	
}

