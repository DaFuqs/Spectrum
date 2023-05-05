package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.storage.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.energy.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.block.*;
import net.minecraft.client.color.block.*;
import net.minecraft.client.color.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;

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
		registerPotionFillables(SpectrumItems.LESSER_POTION_PENDANT, SpectrumItems.GREATER_POTION_PENDANT, SpectrumItems.NIGHTFALLS_BLADE, SpectrumItems.FRACTAL_GLASS_AMPOULE);
		registerSingleInkStorages(SpectrumItems.INK_FLASK);
		registerBrewColors(SpectrumItems.INFUSED_BEVERAGE);
	}
	
	private static void registerColoredLeaves() {
		BlockColorProvider leavesBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
		ItemColorProvider leavesItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.OAK_LEAVES);
		
		if (leavesBlockColorProvider != null && leavesItemColorProvider != null) {
			coloredLeavesBlockColorProvider = new ToggleableBlockColorProvider(leavesBlockColorProvider);
			coloredLeavesItemColorProvider = new ToggleableItemColorProvider(leavesItemColorProvider);
			
			for (DyeColor dyeColor : DyeColor.values()) {
				Block block = ColoredLeavesBlock.byColor(dyeColor);
				ColorProviderRegistry.BLOCK.register(coloredLeavesBlockColorProvider, block);
				ColorProviderRegistry.ITEM.register(coloredLeavesItemColorProvider, block);
			}
		}
	}
	
	private static void registerAmaranth() {
		BlockColorProvider fernBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.FERN);
		ItemColorProvider fernItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.FERN);
		if (fernBlockColorProvider != null && fernItemColorProvider != null) {
			amaranthBushelBlockColorProvider = new ToggleableBlockColorProvider(fernBlockColorProvider);
			amaranthBushelItemColorProvider = new ToggleableItemColorProvider(fernItemColorProvider);
			ColorProviderRegistry.BLOCK.register(amaranthBushelBlockColorProvider, SpectrumBlocks.AMARANTH_BUSHEL);
			ColorProviderRegistry.ITEM.register(amaranthBushelItemColorProvider, SpectrumBlocks.AMARANTH_BUSHEL);
			ColorProviderRegistry.BLOCK.register(amaranthBushelBlockColorProvider, SpectrumBlocks.POTTED_AMARANTH_BUSHEL);
		}
		
		BlockColorProvider largeFernBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.LARGE_FERN);
		ItemColorProvider largeFernItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.LARGE_FERN);
		if (largeFernBlockColorProvider != null && largeFernItemColorProvider != null) {
			amaranthCropBlockColorProvider = new ToggleableBlockColorProvider(largeFernBlockColorProvider);
			amaranthCropItemColorProvider = new ToggleableItemColorProvider(largeFernItemColorProvider);
			ColorProviderRegistry.BLOCK.register(amaranthCropBlockColorProvider, SpectrumBlocks.AMARANTH);
			ColorProviderRegistry.ITEM.register(amaranthCropItemColorProvider, SpectrumBlocks.AMARANTH);
		}
	}
	
	private static void registerClovers(Block... clovers) {
		BlockColorProvider grassBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);
		ItemColorProvider grassItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.GRASS.asItem());
		
		if (grassBlockColorProvider != null && grassItemColorProvider != null) {
			ColorProviderRegistry.BLOCK.register(grassBlockColorProvider, clovers);
		}
	}
	
	private static void registerSingleInkStorages(Item... items) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				InkFlaskItem i = (InkFlaskItem) stack.getItem();
				SingleInkStorage storage = i.getEnergyStorage(stack);
				return ColorHelper.getInt(storage.getStoredColor().getDyeColor());
			}
			return -1;
		}, items);
	}
	
	private static void registerPotionFillables(Item... items) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex > 0) {
				List<InkPoweredStatusEffectInstance> effects = InkPoweredStatusEffectInstance.getEffects(stack);
				if (effects.size() > tintIndex - 1) {
					return effects.get(tintIndex - 1).getColor();
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
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> MemoryItem.getEggColor(stack.getNbt(), tintIndex), memory.asItem());
	}
	
	public static void registerBrewColors(Item brew) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 0) {
				NbtCompound nbt = stack.getNbt();
				return (nbt != null && nbt.contains("Color")) ? nbt.getInt("Color") : 0xf4c6cb;
			}
			return -1;
			
		}, brew);
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