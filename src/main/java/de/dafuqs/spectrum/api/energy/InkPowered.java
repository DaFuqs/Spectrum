package de.dafuqs.spectrum.api.energy;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public interface InkPowered {
	
	/**
	 * The advancement the player needs to have in order to use ink powered tools
	 */
	ResourceLocation REQUIRED_ADVANCEMENT = SpectrumCommon.locate("milestones/unlock_ink_use");
	
	
	@OnlyIn(Dist.CLIENT)
	static boolean canUseClient() {
		Minecraft client = Minecraft.getInstance();
		return canUse(client.player);
	}
	
	static boolean canUse(Player playerEntity) {
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
	@OnlyIn(Dist.CLIENT)
	default void addInkPoweredTooltip(List<Component> tooltip) {
		if (canUseClient()) {
			if (getUsedColors().size() > 1) {
				tooltip.add(Component.translatable("spectrum.tooltip.ink_powered.prefix").withStyle(ChatFormatting.GRAY));
				for (InkColor color : getUsedColors()) {
					tooltip.add(color.getColoredInkName().withStyle(ChatFormatting.GRAY));
				}
			} else {
				tooltip.add(Component.translatable("spectrum.tooltip.ink_powered.consume", getUsedColors().getFirst().getColoredInkName()).withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
	private static long tryDrainEnergy(@NotNull ItemStack stack, InkColor color, long amount, @Nullable Player player) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			if (!inkStorageItem.getDrainability().canDrain(player != null)) {
				return 0;
			}
			
			InkStorage inkStorage = inkStorageItem.getEnergyStorage(stack);
			long drained = inkStorage.drainEnergy(color, amount);
			
			if (drained > 0) {
				if (player instanceof ServerPlayer serverPlayer) {
					SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayer, stack, inkStorage, color, -amount);
				}
				inkStorageItem.setEnergyStorage(stack, inkStorage);
			}
			
			return drained;
		}
		return 0;
	}
	
	private static long tryGetEnergy(@NotNull ItemStack stack, InkColor color) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			return inkStorageItem.getEnergyStorage(stack).getEnergy(color);
		}
		return 0;
	}
	
	/**
	 * Searches an inventory for InkEnergyStorageItems and tries to drain the color energy.
	 * If enough could be drained returns true, else false.
	 * If not enough energy is available it will be drained as much as is available
	 * but return will still be false
	 **/
	static boolean tryDrainEnergy(@NotNull Container inventory, InkColor color, long amount) {
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack currentStack = inventory.getItem(i);
			if (!currentStack.isEmpty()) { // fast fail
				amount -= tryDrainEnergy(currentStack, color, amount, null);
				if (amount <= 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	static boolean tryDrainEnergy(@NotNull Player player, @NotNull InkCost inkCost) {
		return tryDrainEnergy(player, inkCost.color(), inkCost.cost());
	}
	
	static boolean tryDrainEnergy(@NotNull Player player, @NotNull InkCost inkCost, float costModifier) {
		return tryDrainEnergy(player, inkCost.color(), Support.getIntFromDecimalWithChance(inkCost.cost() * costModifier, player.getRandom()));
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
	static boolean tryDrainEnergy(@NotNull Player player, @NotNull InkColor color, long amount) {
		if (player.isCreative()) {
			return true;
		}
		
		if (!canUse(player)) {
			return false;
		}
		
		// hands (main hand, too, if someone uses the staff from the offhand)
		for (ItemStack stack : player.getHandSlots()) {
			amount -= tryDrainEnergy(stack, color, amount, player);
			if (amount <= 0) {
				return true;
			}
		}
		
		// trinket slots
		List<ItemStack> curioInkStorages = CuriosApi
				.getCuriosInventory(player)
				.stream()
				.flatMap(inventory -> inventory.findCurios(itemStack1 -> itemStack1.getItem() instanceof InkStorageItem<?>).stream())
				.map(SlotResult::stack).toList();
		
		for (ItemStack stack : curioInkStorages) {
			amount -= tryDrainEnergy(stack, color, amount, player);
			if (amount <= 0) {
				return true;
			}
		}
		
		// inventory
		for (ItemStack stack : player.getInventory().items) {
			amount -= tryDrainEnergy(stack, color, amount, player);
			if (amount <= 0) {
				return true;
			}
		}
		
		return false;
	}
	
	static boolean hasAvailableInk(Player player, InkCost inkCost) {
		return hasAvailableInk(player, inkCost.color(), inkCost.cost());
	}
	
	static boolean hasAvailableInk(Player player, InkColor color, long amount) {
		if (!canUse(player)) {
			return false;
		}
		
		if (player.isCreative()) {
			return true;
		}
		
		// TODO: make this a status effect tag
		if(SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.MALUM_ID)) {
			MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse("malum:silenced"));
			if (effect != null && player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect))) {
				return false;
			}
		}
		
		// hands
		for (ItemStack stack : player.getHandSlots()) {
			amount -= tryGetEnergy(stack, color);
			if (amount <= 0) {
				return true;
			}
		}
		
		// curio slots
		List<ItemStack> curioInkStorages = CuriosApi
				.getCuriosInventory(player)
				.stream()
				.flatMap(inventory -> inventory.findCurios(itemStack1 -> itemStack1.getItem() instanceof InkStorageItem<?>).stream())
				.map(SlotResult::stack).toList();
		for (ItemStack stack : curioInkStorages) {
			amount -= tryGetEnergy(stack, color);
			if (amount <= 0) {
				return true;
			}
		}
		
		// inventory
		for (ItemStack stack : player.getInventory().items) {
			amount -= tryGetEnergy(stack, color);
			if (amount <= 0) {
				return true;
			}
		}
		
		return false;
	}
	
	static long getAvailableInk(@NotNull Player player, InkColor color) {
		if (player.isCreative()) {
			return Long.MAX_VALUE;
		}
		if (!canUse(player)) {
			return 0;
		}
		
		long available = 0;
		
		// hands
		for (ItemStack stack : player.getHandSlots()) {
			available += tryGetEnergy(stack, color);
		}
		
		// trinket slots
		available += CuriosApi
				.getCuriosInventory(player)
				.stream()
				.flatMap(inventory -> inventory.findCurios(itemStack1 -> itemStack1.getItem() instanceof InkStorageItem<?>).stream())
				.map(SlotResult::stack).mapToLong(stack -> tryGetEnergy(stack, color)).sum();
		
		// inventory
		for (ItemStack stack : player.getInventory().items) {
			available += tryGetEnergy(stack, color);
		}
		return available;
	}
	
	default boolean payForUse(Player player, ItemStack stack, @NotNull InkCost inkCost, @Nullable Ingredient itemCost) {
		boolean paid = player.isCreative(); // free for creative players
		if (!paid) { // try pay with ink
			paid = InkPowered.tryDrainEnergy(player, inkCost, getInkCostMod(player.level().registryAccess(), stack));
		}
		if (!paid && itemCost != null && player.getInventory().contains(itemCost)) {  // try pay with item
			int efficiencyLevel = SpectrumEnchantmentHelper.getLevel(player.level().registryAccess(), Enchantments.EFFICIENCY, stack);
			if (player.getRandom().nextFloat() > (2.0 / (2 + efficiencyLevel))) {
				return true;
			}
			paid = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), itemCost, 1, false) == 1;
		}
		return paid;
	}
	
	default float getInkCostMod(HolderLookup.Provider lookup, ItemStack itemStack) {
		return 3.0F / (3.0F + SpectrumEnchantmentHelper.getLevel(lookup, Enchantments.EFFICIENCY, itemStack));
	}
	
}
