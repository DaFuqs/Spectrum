package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class VariantHelper {
	
	private static final Map<Block, Map<InkColor, Block>> COLORED_STATE_CACHE = new HashMap<>();
	private static final Map<Item, Map<InkColor, Item>> COLORED_ITEM_CACHE = new HashMap<>();
	private static final Map<Block, Block> REPAIRED_STATE_CACHE = new HashMap<>();
	
	public static @Nullable BlockState getColoredBlock(Level world, BlockPos pos, InkColor newColor) {
		if (world.getBlockEntity(pos) != null) {
			return null;
		}
		
		BlockState state = world.getBlockState(pos);
		if (state.is(SpectrumBlockTags.COLORING_BLACKLISTED) || state.getDestroySpeed(world, pos) == -1) {
			return null;
		}
		
		Block block = state.getBlock();
		Block result = recolorRegistryObject(block, newColor, COLORED_STATE_CACHE, BuiltInRegistries.BLOCK::getKey, BuiltInRegistries.BLOCK::get);
		return result != null ? result.withPropertiesOf(state) : null;
	}
	
	public static @Nullable Item getColoredItem(ItemStack stack, InkColor newColor) {
		if (stack.is(SpectrumItemTags.COLORING_BLACKLISTED)) {
			return null;
		}
		Item item = stack.getItem();
		return recolorRegistryObject(item, newColor, COLORED_ITEM_CACHE, BuiltInRegistries.ITEM::getKey, BuiltInRegistries.ITEM::get);
	}
	
	public static @Nullable <T> T recolorRegistryObject(T original, InkColor newColor, Map<T, Map<InkColor, T>> cache, Function<T, ResourceLocation> idGetter, Function<ResourceLocation, T> lookup) {
		Map<InkColor, T> colorMap = cache.computeIfAbsent(original, k -> new HashMap<>());
		return colorMap.computeIfAbsent(newColor, color -> {
			ResourceLocation id = idGetter.apply(original);
			String[] parts = id.getPath().split("_");
			
			for (int i = 0; i < parts.length; i++) {
				int finalI = i;
				InkColor matched = SpectrumRegistries.INK_COLOR.stream()
								.filter(c -> c.toString().equals(parts[finalI]))
								.findFirst()
								.orElse(null);
				
				if (matched != null) {
					parts[i] = color.toString();
					String newPath = String.join("_", parts);
					ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), newPath);
					T newObj = lookup.apply(newId);
					return !newObj.equals(original) ? newObj : null;
				}
			}
			return null;
		});
	}
	
	public static void invalidateCaches() {
		COLORED_STATE_CACHE.clear();
		COLORED_ITEM_CACHE.clear();
		REPAIRED_STATE_CACHE.clear();
	}
	
	//TODO: unused
	public static Block getRepairedBlockVariant(Level world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity != null) {
			return Blocks.AIR;
		}
		
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.is(SpectrumBlockTags.COLORING_BLACKLISTED)) {
			return Blocks.AIR;
		}
		
		Block block = blockState.getBlock();
		
		if (REPAIRED_STATE_CACHE.containsKey(block)) {
			return REPAIRED_STATE_CACHE.get(block);
		}
		
		ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
		
		String newPath = identifier.getPath();
		newPath = newPath.replace("cracked_", "");
		newPath = newPath.replace("damaged_", "");
		newPath = newPath.replace("chipped_", "");
		
		Block returnBlock = Blocks.AIR;
		if (!newPath.equals(identifier.getPath())) {
			ResourceLocation newIdentifier = ResourceLocation.fromNamespaceAndPath(identifier.getNamespace(), newPath);
			Block newIdentifierBlock = BuiltInRegistries.BLOCK.get(newIdentifier);
			if (newIdentifierBlock != block) {
				returnBlock = newIdentifierBlock;
			}
		}
		
		// cache
		REPAIRED_STATE_CACHE.put(block, returnBlock);
		
		return returnBlock;
	}
	
}
