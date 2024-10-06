package de.dafuqs.spectrum.blocks.idols;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FreezingIdolBlock extends IdolBlock {
	
	// Block: The Block to freeze
	// BlockState: The BlockState when Block is getting frozen
	// Float: The chance to freeze
	public static final Map<Block, Pair<BlockState, Float>> FREEZING_MAP = new HashMap<>() {{
		put(Blocks.SNOW, new Pair<>(Blocks.POWDER_SNOW.getDefaultState(), 0.25F));
		put(Blocks.POWDER_SNOW, new Pair<>(Blocks.SNOW_BLOCK.getDefaultState(), 0.5F));
		put(Blocks.WATER, new Pair<>(Blocks.ICE.getDefaultState(), 1.0F));
		put(Blocks.ICE, new Pair<>(Blocks.PACKED_ICE.getDefaultState(), 0.25F));
		put(Blocks.PACKED_ICE, new Pair<>(Blocks.BLUE_ICE.getDefaultState(), 0.1F));
		put(Blocks.GRASS_BLOCK, new Pair<>(Blocks.PODZOL.getDefaultState(), 0.1F));
		put(Blocks.BASALT, new Pair<>(Blocks.CALCITE.getDefaultState(), 0.5F));
		put(SpectrumBlocks.BLAZING_CRYSTAL, new Pair<>(SpectrumBlocks.FROSTBITE_CRYSTAL.getDefaultState(), 0.5F));
	}};
	public static final Map<BlockState, Pair<BlockState, Float>> FREEZING_STATE_MAP = new HashMap<>() {{
		put(Blocks.LAVA.getDefaultState(), new Pair<>(Blocks.OBSIDIAN.getDefaultState(), 1.0F)); // just full, not flowing
	}};
	
	public FreezingIdolBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings, particleEffect);
	}
	
	public static void freeze(@NotNull ServerWorld world, BlockPos blockPos) {
		BlockState sourceState = world.getBlockState(blockPos);
		if (FREEZING_MAP.containsKey(sourceState.getBlock())) {
			Pair<BlockState, Float> recipe = FREEZING_MAP.get(sourceState.getBlock());
			if (recipe.getRight() >= 1.0F || world.random.nextFloat() < recipe.getRight()) {
				// freeze
				world.setBlockState(blockPos, recipe.getLeft());
				world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(recipe.getLeft())); // processed in WorldRenderer processGlobalEvent()
				return;
			}
		}
		if (FREEZING_STATE_MAP.containsKey(sourceState)) {
			Pair<BlockState, Float> recipe = FREEZING_STATE_MAP.get(sourceState);
			if (recipe.getRight() >= 1.0F || world.random.nextFloat() < recipe.getRight()) {
				// freeze
				world.setBlockState(blockPos, recipe.getLeft());
				world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(recipe.getLeft())); // processed in WorldRenderer processGlobalEvent()
			}
		}
		
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		for (Direction direction : Direction.values()) {
			freeze(world, blockPos.offset(direction));
		}
		return true;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.freezing_idol.tooltip"));
	}
	
}
