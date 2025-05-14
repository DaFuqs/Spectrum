package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EnchantmentCanvasItem extends Item {
	
	public EnchantmentCanvasItem(Properties settings) {
		super(settings);
	}
	
	/**
	 * clicked onto another stack
	 */
	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
		if (clickType == ClickAction.SECONDARY) {
			ItemStack otherStack = slot.getItem();
			if (otherStack.getCount() == 1 && tryExchangeEnchantments(stack, otherStack, player)) {
				if (player != null) {
					playExchangeSound(player);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * itemStack is right-clicked onto this
	 */
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		if (clickType == ClickAction.SECONDARY && otherStack.getCount() == 1 && slot.allowModification(player)) {
			if (tryExchangeEnchantments(stack, otherStack, player)) {
				if (player != null) {
					playExchangeSound(player);
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean tryExchangeEnchantments(ItemStack canvasStack, ItemStack targetStack, @Nullable Entity receiver) {
		Optional<Item> itemLock = getItemBoundTo(canvasStack);
		if (itemLock.isPresent() && !targetStack.is(itemLock.get())) {
			return false;
		}
		
		var canvasEnchantments = canvasStack.getOrDefault(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS, ItemEnchantments.EMPTY);
		var targetEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(targetStack);
		if (canvasEnchantments.isEmpty() && targetEnchantments.isEmpty()) {
			return false;
		}
		
		boolean drop = false;
		if (canvasStack.getCount() >= 1) {
			canvasStack = canvasStack.split(1);
			drop = true;
		}
		
		// if the canvas received enchantments: bind it to the other stack
		if (itemLock.isEmpty() && !targetEnchantments.isEmpty()) {
			bindTo(canvasStack, targetStack);
		}
		canvasStack.set(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS, targetEnchantments);
		EnchantmentHelper.setEnchantments(targetStack, canvasEnchantments);
		
		if (drop && receiver != null) {
			if (receiver instanceof Player player) {
				player.getInventory().placeItemBackInInventory(canvasStack);
			} else {
				receiver.spawnAtLocation(canvasStack);
			}
		}
		
		return true;
	}
	
	private void playExchangeSound(Entity entity) {
		entity.playSound(SoundEvents.GRINDSTONE_USE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		Optional<Item> boundItem = getItemBoundTo(stack);
		if (boundItem.isPresent()) {
			tooltip.add(Component.translatable("item.spectrum.enchantment_canvas.tooltip.bound_to").append(boundItem.get().getDescription()));
		} else {
			tooltip.add(Component.translatable("item.spectrum.enchantment_canvas.tooltip.not_bound"));
			tooltip.add(Component.translatable("item.spectrum.enchantment_canvas.tooltip.not_bound2"));
		}
	}
	
	private static void bindTo(ItemStack enchantmentExchangerStack, ItemStack targetStack) {
		enchantmentExchangerStack.set(SpectrumDataComponentTypes.BOUND_ITEM, BuiltInRegistries.ITEM.getKey(targetStack.getItem()));
	}
	
	private static Optional<Item> getItemBoundTo(ItemStack enchantmentExchangerStack) {
		var boundId = enchantmentExchangerStack.get(SpectrumDataComponentTypes.BOUND_ITEM);
		if (boundId == null)
			return Optional.empty();
		return Optional.of(BuiltInRegistries.ITEM.get(boundId));
	}
	
}
