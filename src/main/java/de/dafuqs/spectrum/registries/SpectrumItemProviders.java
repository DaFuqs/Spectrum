package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import net.minecraft.core.component.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class SpectrumItemProviders {
	
	public static void register() {
		ItemProviderRegistry.register(Items.SHULKER_BOX, iterableProvider((player, stack) ->
				stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()));

		ItemProviderRegistry.register(Items.BUNDLE, iterableProvider((player, stack) ->
				stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY).items()));
		
		ItemProviderRegistry.register(SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem(), new ItemProvider() {
			@Override
			public int provideItems(Player player, ItemStack stack, Item requestedItem, int amount) {
				var builder = BottomlessBundleItem.BottomlessStack.Builder.of(player.level(), stack);
				var removed = builder.remove(amount);
				if (!removed.is(requestedItem))
					return 0;
				builder.buildAndSet(stack);
				return removed.getCount();
			}
			
			@Override
			public int getItemCount(Player player, ItemStack stack, Item requestedItem) {
				var bottomlessStack = stack.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessBundleItem.BottomlessStack.DEFAULT);
				if (!bottomlessStack.variant().isOf(requestedItem))
					return 0;
				return (int) Math.min(Integer.MAX_VALUE, bottomlessStack.count());
			}
		});
		
		// BAG_OF_HOLDING only works server side
		// the client does not know about the content of the ender chest, unless opened
		ItemProviderRegistry.register(SpectrumItems.BAG_OF_HOLDING, iterableProvider((player, stack) ->
				player == null ? List.of() : player.getEnderChestInventory().getItems()));
		
	}
	
	public static ItemProvider iterableProvider(BiFunction<@Nullable Player, ItemStack, Iterable<ItemStack>> iterableFactory) {
		return new ItemProvider() {
			@Override
			public int provideItems(Player player, ItemStack stack, Item requestedItem, int amount) {
				int removedCount = 0;
				for (ItemStack s : iterableFactory.apply(player, stack)) {
					if (s.is(requestedItem)) {
						int amountToRemove = Math.min(s.getCount(), amount - removedCount);
						s.shrink(amountToRemove);
						removedCount += amountToRemove;
						if (removedCount == amount) {
							break;
						}
					}
				}
				return removedCount;
			}
			
			@Override
			public int getItemCount(Player player, ItemStack stack, Item requestedItem) {
				int count = 0;
				for (ItemStack s : iterableFactory.apply(player, stack)) {
					if (s.is(requestedItem)) {
						count += s.getCount();
					}
				}
				return count;
			}
		};
	}
	
}