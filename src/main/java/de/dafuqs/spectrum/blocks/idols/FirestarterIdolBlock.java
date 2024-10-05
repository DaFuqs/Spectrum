package de.dafuqs.spectrum.blocks.idols;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.*;
import net.minecraft.server.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FirestarterIdolBlock extends IdolBlock {
	
	// Block: The Block to burn
	// BlockState: The BlockState when Block is getting burnt
	// Float: The chance to burn
	public static final Map<Block, Pair<BlockState, Float>> BURNING_MAP = new HashMap<>() {{
		put(Blocks.RED_MUSHROOM, new Pair<>(Blocks.CRIMSON_FUNGUS.getDefaultState(), 0.2F));
		put(Blocks.BROWN_MUSHROOM, new Pair<>(Blocks.WARPED_FUNGUS.getDefaultState(), 0.2F));
		put(Blocks.SAND, new Pair<>(Blocks.RED_SAND.getDefaultState(), 1.0F));
		put(Blocks.GRASS_BLOCK, new Pair<>(Blocks.DIRT.getDefaultState(), 0.05F));
		put(Blocks.CALCITE, new Pair<>(Blocks.BASALT.getDefaultState(), 0.5F));
		put(Blocks.NETHERRACK, new Pair<>(Blocks.MAGMA_BLOCK.getDefaultState(), 0.25F));
		put(Blocks.MAGMA_BLOCK, new Pair<>(Blocks.LAVA.getDefaultState(), 0.5F));
		put(SpectrumBlocks.FROSTBITE_CRYSTAL, new Pair<>(SpectrumBlocks.BLAZING_CRYSTAL.getDefaultState(), 0.5F));
	}};
	
	public FirestarterIdolBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings, particleEffect);
	}

	public static void addBlockSmeltingRecipes(@NotNull MinecraftServer server) {
		DynamicRegistryManager manager = server.getRegistryManager();
		for (SmeltingRecipe recipe : server.getRecipeManager().listAllOfType(RecipeType.SMELTING)) {
			ItemStack outputStack = recipe.getOutput(manager);
			if (outputStack.getItem() instanceof BlockItem outputBlockItem && outputBlockItem.getBlock() != Blocks.AIR) {
				DefaultedList<Ingredient> ingredients = recipe.getIngredients();
				if (!ingredients.isEmpty()) {
					ItemStack[] inputStacks = ingredients.get(0).getMatchingStacks();
					for (ItemStack inputStack : inputStacks) {
						if (inputStack.getItem() instanceof BlockItem inputBlockItem && inputBlockItem.getBlock() != Blocks.AIR) {
							BURNING_MAP.put(inputBlockItem.getBlock(), new Pair<>(outputBlockItem.getBlock().getDefaultState(), 1.0F));
						}
					}
				}
			}
		}
	}
	
	public static boolean causeFire(@NotNull ServerWorld world, BlockPos blockPos, Direction side) {
		BlockState blockState = world.getBlockState(blockPos);
		if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
			// light lightable blocks
			world.setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
			world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
			return true;
		} else if (blockState.isIn(BlockTags.ICE)) {
			// smelt ice
			world.setBlockState(blockPos, Blocks.WATER.getDefaultState());
			return true;
		} else if (BURNING_MAP.containsKey(blockState.getBlock())) {
			Pair<BlockState, Float> dest = BURNING_MAP.get(blockState.getBlock());
			if (dest.getRight() >= 1.0F || world.random.nextFloat() < dest.getRight()) {
				// convert netherrack to magma blocks
				world.setBlockState(blockPos, dest.getLeft(), 11);
				world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
			}
			return true;
		} else {
			// place fire
			if (AbstractFireBlock.canPlaceAt(world, blockPos, side)) {
				BlockState blockState2 = AbstractFireBlock.getState(world, blockPos);
				world.setBlockState(blockPos, blockState2, 11);
				world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		for (Direction direction : Direction.values()) {
			if (causeFire(world, blockPos.offset(direction), direction)) {
				world.playSound(null, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			}
		}
		return true;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.firestarter_idol.tooltip"));
	}
	
}
