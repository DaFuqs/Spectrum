package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FirestarterIdolBlock extends IdolBlock {
	
	// Block: The Block to burn
	// BlockState: The BlockState when Block is getting burnt
	// Float: The chance to burn
	public static final Map<Block, Tuple<BlockState, Float>> BURNING_MAP = new HashMap<>() {{
		put(Blocks.RED_MUSHROOM, new Tuple<>(Blocks.CRIMSON_FUNGUS.defaultBlockState(), 0.2F));
		put(Blocks.BROWN_MUSHROOM, new Tuple<>(Blocks.WARPED_FUNGUS.defaultBlockState(), 0.2F));
		put(Blocks.SAND, new Tuple<>(Blocks.RED_SAND.defaultBlockState(), 1.0F));
		put(Blocks.SNOW, new Tuple<>(Blocks.AIR.defaultBlockState(), 1.0F));
		put(Blocks.GRASS_BLOCK, new Tuple<>(Blocks.DIRT.defaultBlockState(), 0.05F));
		put(Blocks.CALCITE, new Tuple<>(Blocks.BASALT.defaultBlockState(), 0.5F));
		put(Blocks.NETHERRACK, new Tuple<>(Blocks.MAGMA_BLOCK.defaultBlockState(), 0.25F));
		put(Blocks.MAGMA_BLOCK, new Tuple<>(Blocks.LAVA.defaultBlockState(), 0.5F));
		put(SpectrumBlocks.FROSTBITE_CRYSTAL, new Tuple<>(SpectrumBlocks.BLAZING_CRYSTAL.defaultBlockState(), 0.5F));
	}};
	
	public FirestarterIdolBlock(Properties settings, ParticleOptions particleEffect) {
		super(settings, particleEffect);
	}

	@Override
	public MapCodec<? extends FirestarterIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	public static void addBlockSmeltingRecipes(@NotNull MinecraftServer server) {
		RegistryAccess manager = server.registryAccess();
		for (var recipe : server.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)) {
			ItemStack outputStack = recipe.value().getResultItem(manager);
			if (outputStack.getItem() instanceof BlockItem outputBlockItem && outputBlockItem.getBlock() != Blocks.AIR) {
				NonNullList<Ingredient> ingredients = recipe.value().getIngredients();
				if (!ingredients.isEmpty()) {
					ItemStack[] inputStacks = ingredients.get(0).getItems();
					for (ItemStack inputStack : inputStacks) {
						if (inputStack.getItem() instanceof BlockItem inputBlockItem && inputBlockItem.getBlock() != Blocks.AIR) {
							BURNING_MAP.put(inputBlockItem.getBlock(), new Tuple<>(outputBlockItem.getBlock().defaultBlockState(), 1.0F));
						}
					}
				}
			}
		}
	}
	
	public static boolean causeFire(@NotNull ServerLevel world, BlockPos blockPos, Direction side) {
		BlockState blockState = world.getBlockState(blockPos);
		if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
			// light lightable blocks
			world.setBlock(blockPos, blockState.setValue(BlockStateProperties.LIT, true), 11);
			world.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
			return true;
		} else if (blockState.is(BlockTags.ICE)) {
			// smelt ice
			world.setBlockAndUpdate(blockPos, Blocks.WATER.defaultBlockState());
			return true;
		} else if (BURNING_MAP.containsKey(blockState.getBlock())) {
			Tuple<BlockState, Float> dest = BURNING_MAP.get(blockState.getBlock());
			if (dest.getB() >= 1.0F || world.random.nextFloat() < dest.getB()) {
				// convert netherrack to magma blocks
				world.setBlock(blockPos, dest.getA(), 11);
				world.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
			}
			return true;
		} else {
			// place fire
			if (BaseFireBlock.canBePlacedAt(world, blockPos, side)) {
				BlockState blockState2 = BaseFireBlock.getState(world, blockPos);
				world.setBlock(blockPos, blockState2, 11);
				world.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		for (Direction direction : Direction.values()) {
			if (causeFire(world, blockPos.relative(direction), direction)) {
				world.playSound(null, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			}
		}
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.firestarter_idol.tooltip"));
	}
	
}
