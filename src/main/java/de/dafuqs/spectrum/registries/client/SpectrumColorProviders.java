package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.items.ink.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.event.*;
import org.jspecify.annotations.*;

import java.util.*;

public class SpectrumColorProviders {
	
	public static @Nullable ToggleableBlockColorProvider coloredLeavesBlockColorProvider;
	public static @Nullable ToggleableBlockColorProvider coloredGrassBlockColorProvider;
	public static @Nullable ToggleableBlockColorProvider amaranthBlockColorProvider;
	
	public static @Nullable ToggleableItemColorProvider coloredLeavesItemColorProvider;
	public static @Nullable ToggleableItemColorProvider coloredGrassItemColorProvider;
	public static @Nullable ToggleableItemColorProvider amaranthItemColorProvider;
	
	public static void registerBlocks(RegisterColorHandlersEvent.Block event) {
		// Biome Colors for colored leaves, grass, amaranth and items and blocks
		// They don't use it, but their decoy blocks do
		coloredLeavesBlockColorProvider = new ToggleableBlockColorProvider((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), blockAndTintGetter, blockPos, i));
		coloredGrassBlockColorProvider = new ToggleableBlockColorProvider((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.GRASS_BLOCK.defaultBlockState(), blockAndTintGetter, blockPos, i));
		amaranthBlockColorProvider = new ToggleableBlockColorProvider((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.FERN.defaultBlockState(), blockAndTintGetter, blockPos, i));

		event.register(coloredLeavesBlockColorProvider, ColoredLeavesBlock.all().toArray(new ColoredLeavesBlock[0]));
		event.register(coloredGrassBlockColorProvider, ColoredGrassBlock.all().toArray(new ColoredGrassBlock[0]));
		event.register(coloredGrassBlockColorProvider, ColoredShortGrassBlock.all().toArray(new ColoredShortGrassBlock[0]));
		event.register(coloredGrassBlockColorProvider, ColoredTallGrassBlock.all().toArray(new ColoredTallGrassBlock[0]));
		event.register(amaranthBlockColorProvider, SpectrumBlocks.AMARANTH.get(), SpectrumBlocks.AMARANTH_BUSHEL.get(), SpectrumBlocks.POTTED_AMARANTH_BUSHEL.get());
		
		event.register((blockState, blockAndTintGetter, blockPos, i) -> event.getBlockColors().getColor(Blocks.FERN.defaultBlockState(), blockAndTintGetter, blockPos, i), SpectrumBlocks.CLOVER.get(), SpectrumBlocks.FOUR_LEAF_CLOVER.get());
		
		event.register((state, blockAndTintGetter, pos, tintIndex) -> {
			if (blockAndTintGetter == null || pos == null) {
				return 0x0;
			}
			if (blockAndTintGetter.getBlockEntity(pos) instanceof MemoryBlockEntity memoryBlockEntity) {
				return memoryBlockEntity.getEggColor(tintIndex);
			}
			return 0x0;
		}, SpectrumBlocks.MEMORY.get());
	}
	
	public static void registerItems(RegisterColorHandlersEvent.Item event) {
		coloredLeavesItemColorProvider = new ToggleableItemColorProvider(event, Blocks.OAK_LEAVES.asItem().getDefaultInstance());
		coloredGrassItemColorProvider = new ToggleableItemColorProvider(event, Blocks.GRASS_BLOCK.asItem().getDefaultInstance());
		amaranthItemColorProvider = new ToggleableItemColorProvider(event, Blocks.FERN.asItem().getDefaultInstance());
		
		event.register(coloredLeavesItemColorProvider, ColoredLeavesBlock.allLeaves().toArray(new ColoredLeavesBlock[0]));
		event.register(coloredGrassItemColorProvider, ColoredGrassBlock.all().toArray(new ColoredGrassBlock[0]));
		event.register(coloredGrassItemColorProvider, ColoredShortGrassBlock.all().toArray(new ColoredShortGrassBlock[0]));
		event.register(coloredGrassItemColorProvider, ColoredTallGrassBlock.all().toArray(new ColoredTallGrassBlock[0]));
		event.register(amaranthItemColorProvider, SpectrumBlocks.AMARANTH_BUSHEL, SpectrumBlocks.AMARANTH);
		
		event.register((stack, tintIndex) -> {
			if (tintIndex == 2)
				return 0xFFFFFFFF;
			
			return FastColor.ARGB32.opaque(MemoryItem.getEggColor(stack, tintIndex));
		}, SpectrumBlocks.MEMORY.get());
		
		event.register((stack, tintIndex) -> {
			if (tintIndex > 0) {
				InkPoweredPotionContentsComponent component = stack.get(SpectrumDataComponentTypes.INK_POWERED_POTION_CONTENTS);
				if(component != null) {
					int effectIndex = tintIndex - 1;
					List<InkPoweredMobEffectInstance> effects = component.effects();
					if (effects.size() > effectIndex) {
						return FastColor.ARGB32.opaque(effects.get(effectIndex).getColor());
					}
				}
			}
			return -1;
		}, SpectrumItems.LESSER_POTION_PENDANT.get(), SpectrumItems.GREATER_POTION_PENDANT.get(), SpectrumItems.MALACHITE_GLASS_AMPOULE.get());
		
		event.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				InkPoweredPotionContentsComponent component = stack.get(SpectrumDataComponentTypes.INK_POWERED_POTION_CONTENTS);
				if(component != null && !component.effects().isEmpty()) {
					return FastColor.ARGB32.opaque(component.effects().getFirst().getColor());
				}
			}
			return -1;
		}, SpectrumItems.NIGHTFALLS_BLADE.get(), SpectrumItems.CONCEALING_OILS.get());
		
		event.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				InkFlaskItem i = (InkFlaskItem) stack.getItem();
				SingleInkStorage storage = i.getEnergyStorage(stack);
				return storage.getStoredColor().getColorRGB();
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
				return color == null ? -1 : color.getColorRGB();
			}
			return -1;
		}, SpectrumItems.PAINTBRUSH.get());
	}
	
	public static void resetToggleableProviders() {
		coloredLeavesBlockColorProvider.setShouldApply(true);
		coloredGrassBlockColorProvider.setShouldApply(true);
		amaranthBlockColorProvider.setShouldApply(true);
		
		coloredLeavesItemColorProvider.setShouldApply(true);
		coloredGrassItemColorProvider.setShouldApply(true);
		amaranthItemColorProvider.setShouldApply(true);
	}
	
}