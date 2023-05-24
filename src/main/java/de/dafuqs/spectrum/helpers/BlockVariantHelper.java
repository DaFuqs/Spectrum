package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;

import java.util.*;

public class BlockVariantHelper {
	
	// cache for cursedBlockColorVariant()
	private static final Map<Block, Map<DyeColor, Block>> coloredStates = new HashMap<>();
	// ordered color strings so "light_" variants match before non-light
	private static final List<String> COLOR_STRINGS = List.of("light_blue", "light_gray", "white", "orange", "magenta", "yellow", "lime", "pink", "gray", "cyan", "purple", "blue", "brown", "green", "red", "black");
	
	public static BlockState getCursedBlockColorVariant(World world, BlockPos blockPos, DyeColor newColor) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity != null) {
			return Blocks.AIR.getDefaultState();
		}
		
		BlockState blockState = world.getBlockState(blockPos);
		
		if (blockState.isIn(SpectrumBlockTags.INK_EFFECT_BLACKLISTED)) {
			return Blocks.AIR.getDefaultState();
		}
		
		Block block = blockState.getBlock();
		if (coloredStates.containsKey(block)) {
			Map<DyeColor, Block> colorMap = coloredStates.get(block);
			if (colorMap.containsKey(newColor)) {
				Block newBlock = colorMap.get(newColor);
				return newBlock.getStateWithProperties(blockState);
			}
		}
		
		Identifier identifier = Registries.BLOCK.getId(block);
		
		String newPath = null;
		for (String colorString : COLOR_STRINGS) {
			if (identifier.getPath().contains(colorString)) {
				newPath = identifier.getPath().replace(colorString, newColor.toString());
				break;
			}
		}
		
		Block returnBlock = Blocks.AIR;
		if (newPath != null) {
			Identifier newIdentifier = new Identifier(identifier.getNamespace(), newPath);
			Block newIdentifierBlock = Registries.BLOCK.get(newIdentifier);
			if (newIdentifierBlock != block) {
				returnBlock = newIdentifierBlock;
			}
		}
		
		// cache
		if (coloredStates.containsKey(block)) {
			Map<DyeColor, Block> colorMap = coloredStates.get(block);
			colorMap.put(newColor, returnBlock);
		} else {
			Map<DyeColor, Block> colorMap = new HashMap<>();
			colorMap.put(newColor, returnBlock);
			coloredStates.put(block, colorMap);
		}
		
		return returnBlock.getStateWithProperties(blockState);
	}
	
	// cache for getCursedRepairedBlockVariant()
	private static final Map<Block, Block> repairedStates = new HashMap<>() {{
		put(Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICKS);
		put(Blocks.CRACKED_DEEPSLATE_TILES, Blocks.DEEPSLATE_TILES);
		put(Blocks.CRACKED_NETHER_BRICKS, Blocks.NETHER_BRICKS);
		put(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
		put(Blocks.CRACKED_STONE_BRICKS, Blocks.STONE_BRICKS);
		put(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
		
		put(Blocks.DAMAGED_ANVIL, Blocks.CHIPPED_ANVIL);
		put(Blocks.CHIPPED_ANVIL, Blocks.ANVIL);
		
		put(Blocks.EXPOSED_COPPER, Blocks.COPPER_BLOCK);
		put(Blocks.WEATHERED_COPPER, Blocks.EXPOSED_COPPER);
		put(Blocks.OXIDIZED_COPPER, Blocks.WEATHERED_COPPER);
		put(Blocks.EXPOSED_CUT_COPPER, Blocks.CUT_COPPER);
		put(Blocks.WEATHERED_CUT_COPPER, Blocks.EXPOSED_CUT_COPPER);
		put(Blocks.OXIDIZED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER);
	}};
	
	public static Block getCursedRepairedBlockVariant(World world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity != null) {
			return Blocks.AIR;
		}
		
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isIn(SpectrumBlockTags.INK_EFFECT_BLACKLISTED)) {
			return Blocks.AIR;
		}
		
		Block block = blockState.getBlock();
		
		if (repairedStates.containsKey(block)) {
			return repairedStates.get(block);
		}
		
		Identifier identifier = Registries.BLOCK.getId(block);
		
		String newPath = identifier.getPath();
		newPath = newPath.replace("cracked_", "");
		newPath = newPath.replace("damaged_", "");
		newPath = newPath.replace("chipped_", "");
		
		Block returnBlock = Blocks.AIR;
		if (!newPath.equals(identifier.getPath())) {
			Identifier newIdentifier = new Identifier(identifier.getNamespace(), newPath);
			Block newIdentifierBlock = Registries.BLOCK.get(newIdentifier);
			if (newIdentifierBlock != block) {
				returnBlock = newIdentifierBlock;
			}
		}
		
		// cache
		repairedStates.put(block, returnBlock);
		
		return returnBlock;
	}
	
}
