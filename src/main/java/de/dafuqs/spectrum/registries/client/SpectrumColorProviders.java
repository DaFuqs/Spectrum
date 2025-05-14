package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.items.energy.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.color.block.*;
import net.minecraft.client.color.item.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class SpectrumColorProviders {
	
	public static ToggleableBlockColorProvider coloredLeavesBlockColorProvider;
	public static ToggleableItemColorProvider coloredLeavesItemColorProvider;
	
	public static ToggleableBlockColorProvider amaranthBushelBlockColorProvider;
	public static ToggleableItemColorProvider amaranthBushelItemColorProvider;
	public static ToggleableBlockColorProvider amaranthCropBlockColorProvider;
	public static ToggleableItemColorProvider amaranthCropItemColorProvider;
	
	public static void registerClient() {
		SpectrumCommon.logInfo("Registering Block and Item Color Providers...");
		
		// Biome Colors for colored leaves items and blocks
		// They don't use it, but their decoy oak leaves do
		registerColoredLeaves();
		
		// Same for Amaranth
		registerAmaranth();
		
		registerClovers(SpectrumBlocks.CLOVER, SpectrumBlocks.FOUR_LEAF_CLOVER);
		registerMemory(SpectrumBlocks.MEMORY);
		registerPotionFillables(SpectrumItems.LESSER_POTION_PENDANT, SpectrumItems.GREATER_POTION_PENDANT, SpectrumItems.MALACHITE_GLASS_AMPOULE);
		registerPickyPotionFillables(SpectrumItems.NIGHTFALLS_BLADE, SpectrumItems.CONCEALING_OILS);
		registerSingleInkStorages(SpectrumItems.INK_FLASK);
		registerBrewColors(SpectrumItems.INFUSED_BEVERAGE);
		
		registerOptionalInkColor(SpectrumItems.PAINTBRUSH);
		//registerOptionalInkColor(SpectrumBlocks.CRYSTALLARIEUM.asItem()); // TODO: update item model to use tint layer
	}
	
	private static void registerColoredLeaves() {
		BlockColor leavesBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
		ItemColor leavesItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.OAK_LEAVES);
		
		if (leavesBlockColorProvider != null && leavesItemColorProvider != null) {
			coloredLeavesBlockColorProvider = new ToggleableBlockColorProvider(leavesBlockColorProvider);
			coloredLeavesItemColorProvider = new ToggleableItemColorProvider(leavesItemColorProvider);
			
			for (InkColor color : InkColors.all()) {
				Block block = ColoredLeavesBlock.byColor(color);
				ColorProviderRegistry.BLOCK.register(coloredLeavesBlockColorProvider, block);
				ColorProviderRegistry.ITEM.register(coloredLeavesItemColorProvider, block);
			}
		}
	}
	
	private static void registerAmaranth() {
		BlockColor fernBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.FERN);
		ItemColor fernItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.FERN);
		if (fernBlockColorProvider != null && fernItemColorProvider != null) {
			amaranthBushelBlockColorProvider = new ToggleableBlockColorProvider(fernBlockColorProvider);
			amaranthBushelItemColorProvider = new ToggleableItemColorProvider(fernItemColorProvider);
			ColorProviderRegistry.BLOCK.register(amaranthBushelBlockColorProvider, SpectrumBlocks.AMARANTH_BUSHEL);
			ColorProviderRegistry.ITEM.register(amaranthBushelItemColorProvider, SpectrumBlocks.AMARANTH_BUSHEL);
			ColorProviderRegistry.BLOCK.register(amaranthBushelBlockColorProvider, SpectrumBlocks.POTTED_AMARANTH_BUSHEL);
		}
		
		BlockColor largeFernBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.LARGE_FERN);
		ItemColor largeFernItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.LARGE_FERN);
		if (largeFernBlockColorProvider != null && largeFernItemColorProvider != null) {
			amaranthCropBlockColorProvider = new ToggleableBlockColorProvider(largeFernBlockColorProvider);
			amaranthCropItemColorProvider = new ToggleableItemColorProvider(largeFernItemColorProvider);
			ColorProviderRegistry.BLOCK.register(amaranthCropBlockColorProvider, SpectrumBlocks.AMARANTH);
			ColorProviderRegistry.ITEM.register(amaranthCropItemColorProvider, SpectrumBlocks.AMARANTH);
		}
	}
	
	private static void registerClovers(Block... clovers) {
		BlockColor grassBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.SHORT_GRASS);
		ItemColor grassItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.SHORT_GRASS.asItem());
		
		if (grassBlockColorProvider != null && grassItemColorProvider != null) {
			ColorProviderRegistry.BLOCK.register(grassBlockColorProvider, clovers);
		}
	}
	
	private static void registerSingleInkStorages(Item... items) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				InkFlaskItem i = (InkFlaskItem) stack.getItem();
				SingleInkStorage storage = i.getEnergyStorage(stack);
				return FastColor.ARGB32.opaque(storage.getStoredColor().getColorInt());
			}
			return -1;
		}, items);
	}
	
	private static void registerPickyPotionFillables(Item... items) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				List<InkPoweredStatusEffectInstance> effects = InkPoweredStatusEffectInstance.getEffects(stack);
				if (!effects.isEmpty()) {
					return FastColor.ARGB32.opaque(effects.getFirst().getColor());
				}
			}
			return -1;
		}, items);
	}
	
	private static void registerPotionFillables(Item... items) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex > 0) {
				List<InkPoweredStatusEffectInstance> effects = InkPoweredStatusEffectInstance.getEffects(stack);
				if (effects.size() > tintIndex - 1) {
					return FastColor.ARGB32.opaque(effects.get(tintIndex - 1).getColor());
				}
			}
			return -1;
		}, items);
	}
	
	private static void registerMemory(Block memory) {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			if (world == null) {
				return 0x0;
			}
			if (world.getBlockEntity(pos) instanceof MemoryBlockEntity memoryBlockEntity) {
				return memoryBlockEntity.getEggColor(tintIndex);
			}
			return 0x0;
		}, memory);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 2)
				return 0xFFFFFFFF;
			
			return FastColor.ARGB32.opaque(MemoryItem.getEggColor(stack, tintIndex));
		}, memory.asItem());
	}
	
	public static void registerBrewColors(Item brew) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex != 0) return FastColor.ARGB32.opaque(-1);
			return FastColor.ARGB32.opaque(stack.getOrDefault(SpectrumDataComponentTypes.INFUSED_BEVERAGE, InfusedBeverageComponent.DEFAULT).color());
		}, brew);
	}
	
	public static void registerOptionalInkColor(Item item) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				var color = stack.get(SpectrumDataComponentTypes.INK_COLOR);
				return FastColor.ARGB32.opaque(color == null ? -1 : color.getColorInt());
			}
			return -1;
		}, item);
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