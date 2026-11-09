package de.dafuqs.spectrum.blocks.block_flooder;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public class BlockFlooderBlock extends BaseEntityBlock {
	
	public static final MapCodec<BlockFlooderBlock> CODEC = simpleCodec(BlockFlooderBlock::new);
	
	// when replacing blocks there may be cases when there is a good reason to use replacement blocks
	// like using dirt instead of grass, because grass will be growing anyway and silk touching grass
	// is absolutely not worth it / fun
	// TODO: merge with staff replacements
	public static final HashMap<TagKey<Block>, Block> exchangeableBlocks = new HashMap<>() {{
		put(BlockTags.DIRT, Blocks.DIRT); // grass, podzol, mycelium, ...
		put(BlockTags.BASE_STONE_OVERWORLD, Blocks.STONE);
		put(BlockTags.BASE_STONE_NETHER, Blocks.NETHERRACK);
		put(BlockTags.SAND, Blocks.SAND);
	}};
	public static final List<TagKey<Block>> exchangeBlockTags = ImmutableList.copyOf(exchangeableBlocks.keySet()); // for quick lookup
	public final short MAX_DISTANCE = 10;
	
	public BlockFlooderBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	
	public static boolean isReplaceableBlock(Level world, BlockPos blockPos) {
		BlockState state = world.getBlockState(blockPos);
		Block block = state.getBlock();
		return world.getBlockEntity(blockPos) == null && !(block instanceof BlockFlooderBlock) && (state.isAir() || block instanceof LiquidBlock || state.canBeReplaced() || block instanceof GrowingPlantBodyBlock || block instanceof FlowerBlock);
	}
	
	public static boolean isValidCornerBlock(Level world, BlockPos blockPos) {
		BlockState state = world.getBlockState(blockPos);
		Block block = state.getBlock();
		return state.isRedstoneConductor(world, blockPos) || block instanceof LiquidBlock || block instanceof BlockFlooderBlock;
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onPlace(state, world, pos, oldState, notify);
		if (!world.isClientSide()) {
			world.scheduleTick(pos, state.getBlock(), 4);
		}
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockFlooderBlockEntity(pos, state);
	}
	
	private boolean calculateTargetBlockAndPropagate(BlockState state, Level world, BlockPos pos, RandomSource random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
			Player owner = PlayerOwned.getPlayerIfOnline(world, blockFlooderBlockEntity.getOwnerUUID());
			if (owner == null) {
				world.setBlockAndUpdate(pos, SpectrumConfig.CONFIG.blockFlooderFallbackBlock(world.registryAccess()));
				return false;
			}
			
			Map<Block, Integer> neighboringBlockAmounts = new HashMap<>();
			for (Direction direction : Direction.values()) {
				BlockPos offsetPos = pos.relative(direction);
				BlockState offsetState = world.getBlockState(offsetPos);
				BlockEntity offsetBlockEntity = world.getBlockEntity(offsetPos);
				
				if (offsetBlockEntity != null || offsetState.is(this)) {
					continue;
				}
				
				if (isReplaceableBlock(world, offsetPos)) {
					// air neighbor => spread
					if (blockFlooderBlockEntity.getSourcePos().closerThan(offsetPos.relative(direction), MAX_DISTANCE)
							&& GenericClaimModsCompat.canPlaceBlock(world, offsetPos, owner)
							&& shouldPropagateTo(world, offsetPos)) {
						
						world.setBlockAndUpdate(offsetPos, state);
						if (world.getBlockEntity(offsetPos) instanceof BlockFlooderBlockEntity neighboringBlockFlooderBlockEntity) {
							neighboringBlockFlooderBlockEntity.setOwnerUUID(blockFlooderBlockEntity.getOwnerUUID());
							neighboringBlockFlooderBlockEntity.setSourcePos(blockFlooderBlockEntity.getSourcePos());
							if (blockFlooderBlockEntity.isTargetStateForced()) {
								Block b = removeFromPlayerInventory(owner, blockFlooderBlockEntity.getTargetBlockState().getBlock());
								if (b != null) {
									neighboringBlockFlooderBlockEntity.setTargetStateForced(true);
									neighboringBlockFlooderBlockEntity.setTargetBlockState(b.defaultBlockState());
								}
							}
						}
					}
				} else {
					// set neighbor => remind
					if (offsetState.isRedstoneConductor(world, offsetPos)) {
						Block offsetBlock = offsetState.getBlock();
						neighboringBlockAmounts.put(offsetBlock, neighboringBlockAmounts.getOrDefault(offsetBlock, 0) + 1);
					}
				}
			}
			
			if (blockFlooderBlockEntity.isTargetStateForced()) {
				return true;
			}
			
			if (!neighboringBlockAmounts.isEmpty()) {
				List<Block> mostFrequentBlocks = neighboringBlockAmounts.entrySet().stream()
						.filter(blockIntegerEntry -> !blockIntegerEntry.getKey().defaultBlockState().isAir())
						.sorted((o1, o2) -> {
					if(o1.getValue().equals(o2.getValue())) {
						return (random.nextBoolean() ? o1 : o2).getValue();
					}
					return o1.getValue().compareTo(o2.getValue());
				}).map(Map.Entry::getKey).toList();
				
				for (Block currentBlock : mostFrequentBlocks) {
					Block maxBlock = currentBlock.defaultBlockState().canSurvive(world, pos) ? removeFromPlayerInventory(owner, currentBlock) : null;
					if (maxBlock != null) {
						// and turn this to the leftover block state
						blockFlooderBlockEntity.setTargetBlockState(maxBlock.defaultBlockState());
					} else {
						blockFlooderBlockEntity.setTargetBlockState(SpectrumConfig.CONFIG.blockFlooderFallbackBlock(world.registryAccess()));
					}
				}
			}
		}
		return true;
	}
	
	public static @Nullable Block removeFromPlayerInventory(Player owner, Block currentBlock) {
		Item blockItem = currentBlock.asItem();
		if (blockItem != Items.AIR && InventoryHelper.decrementInPlayerInventory(owner, new ItemStack(blockItem))) {
			return currentBlock;
		}
		
		Optional<TagKey<Block>> tag = Support.getFirstMatchingBlockTag(currentBlock.defaultBlockState(), exchangeBlockTags);
		if (tag.isPresent()) {
			currentBlock = exchangeableBlocks.get(tag.get());
			blockItem = currentBlock.asItem();
			if (blockItem != Items.AIR && InventoryHelper.decrementInPlayerInventory(owner, new ItemStack(blockItem))) {
				return currentBlock;
			}
		}
		return null;
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(state, world, pos, random);
		
		if (!world.isClientSide() && world.getBlockEntity(pos) instanceof BlockFlooderBlockEntity blockEntity) {
			BlockState targetState = blockEntity.getTargetBlockState();
			if (blockEntity.hasSpread()) {
				world.setBlockAndUpdate(pos, targetState);
			} else {
				boolean scheduleUpdate = calculateTargetBlockAndPropagate(state, world, pos, world.getRandom());
				blockEntity.setHasSpread(true);
				if (scheduleUpdate) {
					world.scheduleTick(pos, state.getBlock(), 2 + random.nextInt(5));
				}
			}
		}
	}
	
	private boolean shouldPropagateTo(Level world, BlockPos targetBlockPos) {
		if (isReplaceableBlock(world, targetBlockPos)) {
			int count = 0;
			for (Direction direction : Direction.values()) {
				for (int i = 1; i < MAX_DISTANCE / 2; i++) {
					BlockPos offsetPos = targetBlockPos.relative(direction, i);
					if (isValidCornerBlock(world, offsetPos)) {
						count++;
						break;
					}
				}
			}
			return (count >= 4);
		}
		return false;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		if(builder.getParameter(LootContextParams.BLOCK_ENTITY) instanceof BlockFlooderBlockEntity blockEntity) {
			return List.of(new ItemStack(blockEntity.getTargetBlockState().getBlock().asItem()));
		}
		return super.getDrops(state, builder);
	}
	
}
