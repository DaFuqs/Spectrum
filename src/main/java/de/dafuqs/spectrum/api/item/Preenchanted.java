package de.dafuqs.spectrum.api.item;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.event.*;
import org.jetbrains.annotations.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

@EventBusSubscriber
public interface Preenchanted {
	
	@ApiStatus.Internal
	Random random = new Random();
	
	Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments();
	static ItemEnchantments buildDefaultEnchantments(HolderLookup.Provider lookup, Preenchanted item) {
		ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		for (Map.Entry<ResourceKey<Enchantment>, Integer> entry : item.getDefaultEnchantments().entrySet()) {
			builder.set(lookup.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(entry.getKey()), entry.getValue());
		}
		return builder.toImmutable();
	}
	
	static <T extends Item & Preenchanted> ItemStack getDefaultEnchantedStack(HolderLookup.Provider lookup, T item) {
		ItemStack stack = new ItemStack(item);
		stack.set(DataComponents.ENCHANTMENTS, buildDefaultEnchantments(lookup, item));
		return stack;
	}
	
	/**
	 * Checks a stack if it only has enchantments that are lower or equal its DefaultEnchantments,
	 * meaning enchantments had been added on top of the original ones.
	 */
	default boolean onlyHasPreEnchantments(ItemStack stack) {
		ItemEnchantments stackEnchants = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		
		Map<ResourceKey<Enchantment>, Integer> defaultEnchants = new Object2IntArrayMap<>(getDefaultEnchantments());
		for(Object2IntMap.Entry<Holder<Enchantment>> stackEnchantEntry : stackEnchants.entrySet()) {
			@Nullable ResourceKey<Enchantment> stackEnchantKey = stackEnchantEntry.getKey().getKey();
			int stackEnchantLevel = defaultEnchants.getOrDefault(stackEnchantKey, 0);
			if(stackEnchantLevel == stackEnchantEntry.getIntValue()) {
				defaultEnchants.remove(stackEnchantKey);
			} else {
				return false;
			}
		}
		
		return defaultEnchants.isEmpty();
	}
	
	@ApiStatus.Internal
	@SubscribeEvent
	static void onGrindStonePlace(GrindstoneEvent.OnPlaceItem event) {
		if(!event.getTopItem().isEmpty() && !event.getBottomItem().isEmpty()) {
			return;
		}
		Preenchanted itemInstance = null;
		ItemStack stack = null;
		if (event.getTopItem().getItem() instanceof Preenchanted preenchanted) {
			itemInstance = preenchanted;
			stack = event.getTopItem();
		}
		if (event.getBottomItem().getItem() instanceof Preenchanted preenchanted) {
			itemInstance = preenchanted;
			stack = event.getBottomItem();
		}
		if (itemInstance == null) {
			return;
		}
		ItemEnchantments itemEnchantments = stack.get(DataComponents.ENCHANTMENTS);
		if (itemEnchantments == null) {
			return;
		}
		ItemStack output = stack.copy();
		ItemEnchantments.Mutable outputBuilder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		int xp = 0;
		for (Object2IntMap.Entry<Holder<Enchantment>> existingEnchantment : itemEnchantments.entrySet()) {
			Holder<Enchantment> holder = existingEnchantment.getKey();
			Integer defaultLevel = itemInstance.getDefaultEnchantments().get(holder.getKey());
			int existingLevel = existingEnchantment.getIntValue();
			if (defaultLevel != null && defaultLevel <= existingLevel) {
				outputBuilder.set(existingEnchantment.getKey(), defaultLevel);
				if (!holder.is(EnchantmentTags.CURSE)){
					xp += holder.value().getMinCost(existingLevel) - holder.value().getMinCost(defaultLevel);
				}
			} else if (!holder.is(EnchantmentTags.CURSE)){
				xp += holder.value().getMinCost(existingLevel);
			}
		}
		output.set(DataComponents.ENCHANTMENTS, outputBuilder.toImmutable());
		event.setOutput(output);
		if (xp == 0) {
			event.setXp(xp);
			return;
		}
		xp = (int)Math.ceil(xp / 2.0);
		event.setXp( xp + random.nextInt(xp));
	}
}
