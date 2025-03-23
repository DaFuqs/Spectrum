package de.dafuqs.spectrum.api.energy;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface InkPowered {
	
	/**
	 * The advancement the player needs to have in order to use ink powered tools
	 */
	Identifier REQUIRED_ADVANCEMENT = SpectrumCommon.locate("milestones/unlock_ink_use");
	
	@Environment(EnvType.CLIENT)
    static boolean canUseClient() {
		MinecraftClient client = MinecraftClient.getInstance();
		return canUse(client.player);
	}
	
	static boolean canUse(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, InkPowered.REQUIRED_ADVANCEMENT);
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
	@Environment(EnvType.CLIENT)
	default void addInkPoweredTooltip(List<Text> tooltip) {
		if (canUseClient()) {
			if (getUsedColors().size() > 1) {
				tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.prefix").formatted(Formatting.GRAY));
				for (InkColor color : getUsedColors()) {
					tooltip.add(color.getColoredInkName().formatted(Formatting.GRAY));
				}
			} else {
				tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.consume", getUsedColors().get(0).getColoredInkName()).formatted(Formatting.GRAY));
			}
		}
	}
	
	private static long tryDrainEnergy(@NotNull ItemStack stack, InkColor color, long amount, @Nullable PlayerEntity player) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			if (!inkStorageItem.getDrainability().canDrain(player != null)) {
				return 0;
			}
			
			InkStorage inkStorage = inkStorageItem.getEnergyStorage(stack);
			long drained = inkStorage.drainEnergy(color, amount);
			if (drained > 0) {
				if (player instanceof ServerPlayerEntity serverPlayerEntity) {
					SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, inkStorage, color, -amount);
				}
				
				inkStorageItem.setEnergyStorage(stack, inkStorage);
			}
			return drained;
		} else {
			return 0;
		}
	}
	
	private static long tryGetEnergy(@NotNull ItemStack stack, InkColor color) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			InkStorage inkStorage = inkStorageItem.getEnergyStorage(stack);
			return inkStorage.getEnergy(color);
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
	static boolean tryDrainEnergy(@NotNull Inventory inventory, InkColor color, long amount) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack currentStack = inventory.getStack(i);
			if (!currentStack.isEmpty()) { // fast fail
				amount -= tryDrainEnergy(currentStack, color, amount, null);
				if (amount <= 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	static boolean tryDrainEnergy(@NotNull PlayerEntity player, @NotNull InkCost inkCost) {
		return tryDrainEnergy(player, inkCost.getColor(), inkCost.getCost());
	}
	
	static boolean tryDrainEnergy(@NotNull PlayerEntity player, @NotNull InkCost inkCost, float costModifier) {
		return tryDrainEnergy(player, inkCost.getColor(), Support.getIntFromDecimalWithChance(inkCost.getCost() * costModifier, player.getRandom()));
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
	static boolean tryDrainEnergy(@NotNull PlayerEntity player, @NotNull InkColor color, long amount) {
		if (player.isCreative()) {
			return true;
		}
		if (!canUse(player)) {
			return false;
		}
		if(SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.MALUM_ID) && player.hasStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("malum:silenced"))))
		{
			return false;
		}
		
		// hands (main hand, too, if someone uses the staff from the offhand)
		for (ItemStack itemStack : player.getHandItems()) {
			amount -= tryDrainEnergy(itemStack, color, amount, player);
			if (amount <= 0) {
				return true;
			}
		}
		
		// trinket slots
		Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(player);
		if (optionalTrinketComponent.isPresent()) {
			List<Pair<SlotReference, ItemStack>> trinketInkStorages = optionalTrinketComponent.get().getEquipped(itemStack -> itemStack.getItem() instanceof InkStorageItem<?>);
			for (Pair<SlotReference, ItemStack> trinketEnergyStorageStack : trinketInkStorages) {
				amount -= tryDrainEnergy(trinketEnergyStorageStack.getRight(), color, amount, player);
				if (amount <= 0) {
					return true;
				}
			}
		}
		
		// inventory
		for (ItemStack itemStack : player.getInventory().main) {
			amount -= tryDrainEnergy(itemStack, color, amount, player);
			if (amount <= 0) {
				return true;
			}
		}
		
		return false;
	}
	
	static long getAvailableInk(@NotNull PlayerEntity player, InkColor color) {
		if (player.isCreative()) {
			return Long.MAX_VALUE;
		}
		if (!canUse(player)) {
			return 0;
		}
		
		long available = 0;
		
		// hands
		for (ItemStack itemStack : player.getHandItems()) {
			available += tryGetEnergy(itemStack, color);
		}
		
		// trinket slots
		Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(player);
		if (optionalTrinketComponent.isPresent()) {
			List<Pair<SlotReference, ItemStack>> trinketInkStorages = optionalTrinketComponent.get().getEquipped(itemStack -> itemStack.getItem() instanceof InkStorageItem<?>);
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
	
	static boolean hasAvailableInk(PlayerEntity player, InkCost inkCost) {
		return hasAvailableInk(player, inkCost.getColor(), inkCost.getCost());
	}
	
	static boolean hasAvailableInk(PlayerEntity player, InkColor color, long amount) {
		if (!canUse(player)) {
			return false;
		}

		if(SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.MALUM_ID) && player.hasStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("malum:silenced"))))
		{
			return false;
		}
		
		if (player.isCreative()) {
			return true;
		}
		
		// hands
		for (ItemStack itemStack : player.getHandItems()) {
			amount -= tryGetEnergy(itemStack, color);
			if (amount <= 0) {
				return true;
			}
		}
		
		// trinket slot
		Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(player);
		if (optionalTrinketComponent.isPresent()) {
			List<Pair<SlotReference, ItemStack>> trinketInkStorages = optionalTrinketComponent.get().getEquipped(itemStack -> itemStack.getItem() instanceof InkStorageItem<?>);
			for (Pair<SlotReference, ItemStack> trinketEnergyStorageStack : trinketInkStorages) {
				amount -= tryGetEnergy(trinketEnergyStorageStack.getRight(), color);
				if (amount <= 0) {
					return true;
				}
			}
		}
		
		// inventory
		for (ItemStack itemStack : player.getInventory().main) {
			amount -= tryGetEnergy(itemStack, color);
			if (amount <= 0) {
				return true;
			}
		}
		
		return false;
	}
	
}
