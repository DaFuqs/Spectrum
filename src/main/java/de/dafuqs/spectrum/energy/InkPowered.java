package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.helpers.Support;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
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
	
	/**
	 * The advancement the player needs to have in order to use ink powered tools
	 */
	Identifier REQUIRED_ADVANCEMENT = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_ink_use");
	
	@Environment(EnvType.CLIENT)
	static boolean canUse() {
		return Support.hasAdvancement(MinecraftClient.getInstance().player, InkPowered.REQUIRED_ADVANCEMENT);
	}
	
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
            if (getUsedColors().size() > 1) {
                tooltip.add(new TranslatableText("spectrum.tooltip.ink_powered.prefix").formatted(Formatting.GRAY));
                for (InkColor color : getUsedColors()) {
                    tooltip.add(new TranslatableText("spectrum.tooltip.ink_powered.bullet." + color.toString()));

                }
            } else {
                tooltip.add(new TranslatableText("spectrum.tooltip.ink_powered." + getUsedColors().get(0).toString()).formatted(Formatting.GRAY));
            }
        }
	}
	
	static long tryDrainEnergy(ItemStack stack, InkColor color, long amount) {
		if (stack.getItem() instanceof InkStorageItem inkStorageItem) {
			InkStorage inkStorage = inkStorageItem.getEnergyStorage(stack);
			long drained = inkStorage.drainEnergy(color, amount);
			if(drained > 0) {
				inkStorageItem.setEnergyStorage(stack, inkStorage);
			}
			return drained;
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
	static boolean tryPayCost(Inventory inventory, InkColor color, long amount) {
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
	static boolean tryPayCost(ServerPlayerEntity player, InkColor color, long amount) {
		if(!Support.hasAdvancement(player, REQUIRED_ADVANCEMENT)) {
			return false;
		}
		
		// hands (main hand, too, if someone uses the staff from the offhand)
		for (ItemStack itemStack : player.getItemsHand()) {
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
	
	static long getAvailableInk(PlayerEntity player, InkColor color) {
		if(!Support.hasAdvancement(player, REQUIRED_ADVANCEMENT)) {
			return 0;
		}
		
		long available = 0;
		// offhand
		for (ItemStack itemStack : player.getInventory().offHand) {
			available += tryGetEnergy(itemStack, color);
		}
		
		// trinket slot
		Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(player);
		if (optionalTrinketComponent.isPresent()) {
			List<Pair<SlotReference, ItemStack>> trinketInkStorages = optionalTrinketComponent.get().getEquipped(itemStack -> itemStack.getItem() instanceof InkStorage);
			for (Pair<SlotReference, ItemStack> trinketEnergyStorageStack : trinketInkStorages) {
				available += tryGetEnergy(trinketEnergyStorageStack.getRight(), color);
			}
		}
		
		// inventory
		for (ItemStack itemStack : player.getInventory().main) {
			available += tryGetEnergy(itemStack, color);
		}
		return available;
	}
	
	static long tryGetEnergy(ItemStack stack, InkColor color) {
		if (stack.getItem() instanceof InkStorageItem inkStorageItem) {
			InkStorage inkStorage = inkStorageItem.getEnergyStorage(stack);
			return inkStorage.getEnergy(color);
		} else {
			return 0;
		}
	}
	
}

