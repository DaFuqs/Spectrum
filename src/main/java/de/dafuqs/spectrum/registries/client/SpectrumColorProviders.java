package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.memory.MemoryBlockEntity;
import de.dafuqs.spectrum.blocks.memory.MemoryItem;
import de.dafuqs.spectrum.energy.storage.SingleInkStorage;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.items.magic.InkFlaskItem;
import de.dafuqs.spectrum.progression.ToggleableBlockColorProvider;
import de.dafuqs.spectrum.progression.ToggleableItemColorProvider;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.DyeColor;

import java.util.List;

public class SpectrumColorProviders {
	
	public static ToggleableBlockColorProvider coloredLeavesBlockColorProvider;
	public static ToggleableItemColorProvider coloredLeavesItemColorProvider;
	
	public static void registerClient() {
		SpectrumCommon.logInfo("Registering Block and Item Color Providers...");
		
		// Biome Colors for colored leaves items and blocks
		// They don't use it, but their decay as oak leaves do
		BlockColorProvider leavesBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
		ItemColorProvider leavesItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.OAK_LEAVES);
		
		if (leavesBlockColorProvider != null && leavesItemColorProvider != null) {
			coloredLeavesBlockColorProvider = new ToggleableBlockColorProvider(leavesBlockColorProvider);
			coloredLeavesItemColorProvider = new ToggleableItemColorProvider(leavesItemColorProvider);
			
			for (DyeColor dyeColor : DyeColor.values()) {
				Block block = SpectrumBlocks.getColoredLeavesBlock(dyeColor);
				ColorProviderRegistry.BLOCK.register(coloredLeavesBlockColorProvider, block);
				ColorProviderRegistry.ITEM.register(coloredLeavesItemColorProvider, block);
			}
		}
		
		BlockColorProvider grassBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);
		ItemColorProvider grassItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.GRASS.asItem());
		
		if (grassBlockColorProvider != null && grassItemColorProvider != null) {
			ColorProviderRegistry.BLOCK.register(grassBlockColorProvider, SpectrumBlocks.CLOVER);
			ColorProviderRegistry.BLOCK.register(grassBlockColorProvider, SpectrumBlocks.FOUR_LEAF_CLOVER);
		}
		
		// MEMORIES
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			if (world == null) {
				return 0x0;
			}
			
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
				return memoryBlockEntity.getEggColor(tintIndex);
			}
			
			return 0x0;
		}, SpectrumBlocks.MEMORY);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> MemoryItem.getEggColor(stack.getNbt(), tintIndex), SpectrumBlocks.MEMORY.asItem());
		
		// Potion Pendant Potion Color Overlays
		ColorProviderRegistry.ITEM.register(SpectrumColorProviders::potionColor, SpectrumItems.LESSER_POTION_PENDANT);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex != 0 && tintIndex < 4) {
				List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
				if (tintIndex == 1) {
					if (effects.size() > 0) {
						return effects.get(0).getEffectType().getColor();
					}
				} else if (tintIndex == 2) {
					if (effects.size() > 1) {
						return effects.get(1).getEffectType().getColor();
					}
				} else {
					if (effects.size() > 2) {
						return effects.get(2).getEffectType().getColor();
					}
				}
			}
			return -1;
		}, SpectrumItems.GREATER_POTION_PENDANT);
		
		
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				InkFlaskItem i = (InkFlaskItem) stack.getItem();
				SingleInkStorage storage = i.getEnergyStorage(stack);
				return ColorHelper.getInt(storage.getStoredColor().getDyeColor());
			}
			return -1;
		}, SpectrumItems.INK_FLASK);
	}
	
	public static int potionColor(ItemStack stack, int tintIndex) {
		if (tintIndex == 1) {
			NbtCompound nbtCompound = stack.getNbt();
			if (nbtCompound != null && nbtCompound.contains("CustomPotionColor", NbtElement.INT_TYPE)) {
				return nbtCompound.getInt("CustomPotionColor");
			}
			
			List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
			if (effects.size() > 0) {
				return PotionUtil.getColor(effects);
			}
		}
		return -1;
	}
}