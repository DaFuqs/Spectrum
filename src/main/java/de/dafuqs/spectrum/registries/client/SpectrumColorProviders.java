package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.items.energy.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.color.item.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.event.*;

import java.util.*;

public class SpectrumColorProviders {
	
	public static ToggleableBlockColorProvider coloredLeavesBlockColorProvider;
	public static ToggleableItemColorProvider coloredLeavesItemColorProvider;
	
	public static ToggleableBlockColorProvider amaranthBushelBlockColorProvider;
	public static ToggleableItemColorProvider amaranthBushelItemColorProvider;
	public static ToggleableBlockColorProvider amaranthCropBlockColorProvider;
	public static ToggleableItemColorProvider amaranthCropItemColorProvider;
	
	public static void registerBlocks(RegisterColorHandlersEvent.Block event) {
		// Biome Colors for colored leaves items and blocks
		// They don't use it, but their decoy oak leaves do
		
		coloredLeavesBlockColorProvider = new ToggleableBlockColorProvider((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), blockAndTintGetter, blockPos, i));
		
		for (InkColor color : InkColors.all()) {
			Block block = ColoredLeavesBlock.byColor(color);
			event.register(coloredLeavesBlockColorProvider, block);
		}
		
		amaranthCropBlockColorProvider = new ToggleableBlockColorProvider((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.FERN.defaultBlockState(), blockAndTintGetter, blockPos, i));
		amaranthBushelBlockColorProvider = new ToggleableBlockColorProvider((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.FERN.defaultBlockState(), blockAndTintGetter, blockPos, i));
		event.register(amaranthCropBlockColorProvider, SpectrumBlocks.AMARANTH.get());
		event.register(amaranthBushelBlockColorProvider, SpectrumBlocks.AMARANTH_BUSHEL.get());
		event.register(amaranthBushelBlockColorProvider, SpectrumBlocks.POTTED_AMARANTH_BUSHEL.get());
		
		event.register((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.FERN.defaultBlockState(), blockAndTintGetter, blockPos, i), SpectrumBlocks.CLOVER.get(), SpectrumBlocks.FOUR_LEAF_CLOVER.get());
		
		event.register((state, blockAndTintGetter, pos, tintIndex) -> {
			if (blockAndTintGetter == null) {
				return 0x0;
			}
			if (blockAndTintGetter.getBlockEntity(pos) instanceof MemoryBlockEntity memoryBlockEntity) {
				return memoryBlockEntity.getEggColor(tintIndex);
			}
			return 0x0;
		}, SpectrumBlocks.MEMORY.get());
	}
	
	public static void registerItems(RegisterColorHandlersEvent.Item event) {
		// Biome Colors for colored leaves items and blocks
		// They don't use it, but their decoy oak leaves do
		ItemColor leavesItemColorProvider = ItemColors.getItemColors(Blocks.OAK_LEAVES.asItem());
		if (leavesItemColorProvider != null) {
			coloredLeavesBlockColorProvider = new ToggleableBlockColorProvider(leavesBlockColorProvider);
			coloredLeavesItemColorProvider = new ToggleableItemColorProvider(leavesItemColorProvider);
			
			for (InkColor color1 : InkColors.all()) {
				Block block = ColoredLeavesBlock.byColor(color1);
				ColorProviderRegistry.ITEM.register(coloredLeavesItemColorProvider, block);
			}
		}
		
		// Same for Amaranth
		ItemColor fernItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.FERN);
		if (fernItemColorProvider != null) {
			amaranthBushelBlockColorProvider = new ToggleableBlockColorProvider(fernBlockColorProvider);
			amaranthBushelItemColorProvider = new ToggleableItemColorProvider(fernItemColorProvider);
			ColorProviderRegistry.ITEM.register(amaranthBushelItemColorProvider, SpectrumBlocks.AMARANTH_BUSHEL);
		}
		
		ItemColor largeFernItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.LARGE_FERN);
		if (largeFernItemColorProvider != null) {
			amaranthCropBlockColorProvider = new ToggleableBlockColorProvider(largeFernBlockColorProvider);
			amaranthCropItemColorProvider = new ToggleableItemColorProvider(largeFernItemColorProvider);
			ColorProviderRegistry.ITEM.register(amaranthCropItemColorProvider, SpectrumBlocks.AMARANTH);
		}
		
		event.register((stack, tintIndex) -> {
			if (tintIndex == 2)
				return 0xFFFFFFFF;
			
			return FastColor.ARGB32.opaque(MemoryItem.getEggColor(stack, tintIndex));
		}, SpectrumBlocks.MEMORY.get());
		
		event.register((stack, tintIndex) -> {
			if (tintIndex > 0) {
				List<InkPoweredStatusEffectInstance> effects = InkPoweredStatusEffectInstance.getEffects(stack);
				if (effects.size() > tintIndex - 1) {
					return FastColor.ARGB32.opaque(effects.get(tintIndex - 1).getColor());
				}
			}
			return -1;
		}, new Item[]{SpectrumItems.LESSER_POTION_PENDANT.get(), SpectrumItems.GREATER_POTION_PENDANT.get(), SpectrumItems.MALACHITE_GLASS_AMPOULE.get()});
		
		event.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				List<InkPoweredStatusEffectInstance> effects = InkPoweredStatusEffectInstance.getEffects(stack);
				if (!effects.isEmpty()) {
					return FastColor.ARGB32.opaque(effects.getFirst().getColor());
				}
			}
			return -1;
		}, new Item[]{SpectrumItems.NIGHTFALLS_BLADE.get(), SpectrumItems.CONCEALING_OILS.get()});
		
		event.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				InkFlaskItem i = (InkFlaskItem) stack.getItem();
				SingleInkStorage storage = i.getEnergyStorage(stack);
				return FastColor.ARGB32.opaque(storage.getStoredColor().getColorInt());
			}
			return -1;
		}, SpectrumItems.INK_FLASK.get());
		
		event.register((stack, tintIndex) -> {
			if (tintIndex != 0) return FastColor.ARGB32.opaque(-1);
			return FastColor.ARGB32.opaque(stack.getOrDefault(SpectrumDataComponentTypes.INFUSED_BEVERAGE, InfusedBeverageComponent.DEFAULT).color());
		}, SpectrumItems.INFUSED_BEVERAGE.get());
		
		event.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				var color = stack.get(SpectrumDataComponentTypes.INK_COLOR);
				return FastColor.ARGB32.opaque(color == null ? -1 : color.getColorInt());
			}
			return -1;
		}, SpectrumItems.PAINTBRUSH.get());
	}
	
	public static void resetToggleableProviders() {
		coloredLeavesBlockColorProvider.setShouldApply(true);
		coloredLeavesItemColorProvider.setShouldApply(true);
		
		amaranthBushelBlockColorProvider.setShouldApply(true);
		amaranthBushelItemColorProvider.setShouldApply(true);
		amaranthCropBlockColorProvider.setShouldApply(true);
		amaranthCropItemColorProvider.setShouldApply(true);
	}
	
}