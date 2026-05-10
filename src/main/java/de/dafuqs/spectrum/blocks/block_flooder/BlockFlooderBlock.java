package de.dafuqs.spectrum.blocks.block_flooder;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import javax.annotation.*;

import java.util.*;

public class BlockFlooderBlock extends BaseEntityBlock {
	
	public static final MapCodec<BlockFlooderBlock> CODEC = simpleCodec(BlockFlooderBlock::new);
	
	// when replacing blocks there may be cases when there is a good reason to use replacement blocks
	// like using dirt instead of grass, because grass will be growing anyway and silk touching grass
	// is absolutely not worth it / fun
	public static final HashMap<TagKey<Block>, Block> exchangeableBlocks = new HashMap<>() {{
		put(BlockTags.DIRT, Blocks.DIRT); // grass, podzol, mycelium, ...
		put(BlockTags.BASE_STONE_OVERWORLD, Blocks.STONE);
		put(BlockTags.BASE_STONE_NETHER, Blocks.NETHERRACK);
		put(BlockTags.SAND, Blocks.SAND);
	}};
	public static final List<TagKey<Block>> exchangeBlockTags = ImmutableList.copyOf(exchangeableBlocks.keySet()); // for quick lookup
	public final short MAX_DISTANCE = 10;
	public final BlockState DEFAULT_BLOCK_STATE = Blocks.COBBLESTONE.defaultBlockState();
	
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
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockFlooderBlockEntity(pos, state);
	}
	
	private boolean calculateTargetBlockAndPropagate(BlockState state, Level world, BlockPos pos, RandomSource random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
			Player owner = PlayerOwned.getPlayerIfOnline(world, blockFlooderBlockEntity.getOwnerUUID());
			if (owner == null) {
				world.setBlock(pos, DEFAULT_BLOCK_STATE, 3);
				return false;
			}
			
			Map<Block, Integer> neighboringBlockAmounts = new HashMap<>();
			for (Direction direction : Direction.values()) {
				BlockPos targetBlockPos = pos.relative(direction);
				BlockState currentBlockState = world.getBlockState(targetBlockPos);
				BlockEntity currentBlockEntity = world.getBlockEntity(targetBlockPos);
				
				if (!currentBlockState.is(this) && currentBlockEntity == null) {
					if (isReplaceableBlock(world, targetBlockPos)) {
						Vec3i nextPos = new Vec3i(targetBlockPos.relative(direction).getX(), targetBlockPos.relative(direction).getY(), targetBlockPos.relative(direction).getZ());
						if (blockFlooderBlockEntity.getSourcePos().closerThan(nextPos, MAX_DISTANCE)
								&& GenericClaimModsCompat.canPlaceBlock(world, targetBlockPos, owner)
								&& shouldPropagateTo(world, targetBlockPos)) {
							
							world.setBlock(targetBlockPos, state, 3);
							if (world.getBlockEntity(targetBlockPos) instanceof BlockFlooderBlockEntity neighboringBlockFlooderBlockEntity) {
								neighboringBlockFlooderBlockEntity.setOwnerUUID(blockFlooderBlockEntity.getOwnerUUID());
								neighboringBlockFlooderBlockEntity.setSourcePos(blockFlooderBlockEntity.getSourcePos());
							}
						}
					} else {
						Block currentBlock = currentBlockState.getBlock();
						
						if (currentBlockState.isRedstoneConductor(world, targetBlockPos)) {
							if (neighboringBlockAmounts.containsKey(currentBlock)) {
								neighboringBlockAmounts.put(currentBlock, neighboringBlockAmounts.get(currentBlock) + 1);
							} else {
								neighboringBlockAmounts.put(currentBlock, 1);
							}
						}
					}
				}
			}
			
			if (!neighboringBlockAmounts.isEmpty()) {
				int max = 0;
				Block maxBlock = null;
				
				for (Map.Entry<Block, Integer> entry : neighboringBlockAmounts.entrySet()) {
					Block currentBlock = entry.getKey();
					int currentOccurrences = entry.getValue();
					Item blockItem = currentBlock.asItem();
					
					if (blockItem != Items.AIR) {
						if (currentOccurrences > max || (currentOccurrences == max && random.nextBoolean())) {
							ItemStack currentItemStack = new ItemStack(blockItem);
							if (owner.isCreative() || owner.getInventory().contains(currentItemStack) && currentBlock.defaultBlockState().canSurvive(world, pos)) {
								maxBlock = currentBlock;
							} else {
								Optional<TagKey<Block>> tag = Support.getFirstMatchingBlockTag(currentBlock.defaultBlockState(), exchangeBlockTags);
								if (tag.isPresent()) {
									currentBlock = exchangeableBlocks.get(tag.get());
									blockItem = currentBlock.asItem();
									if (blockItem != Items.AIR) {
										currentItemStack = new ItemStack(blockItem);
										if (owner.isCreative() || owner.getInventory().contains(currentItemStack) && currentBlock.defaultBlockState().canSurvive(world, pos)) {
											maxBlock = currentBlock;
										}
									}
								}
							}
						}
					}
				}
				
				if (maxBlock != null) {
					// and turn this to the leftover block state
					blockFlooderBlockEntity.setTargetBlockState(maxBlock.defaultBlockState());
				} else {
					blockFlooderBlockEntity.setTargetBlockState(DEFAULT_BLOCK_STATE);
				}
			}
		}
		return true;
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(state, world, pos, random);
		
		if (!world.isClientSide()) {
			if (world.getBlockEntity(pos) instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
				BlockState targetState = blockFlooderBlockEntity.getTargetBlockState();
				if (targetState == null || targetState.isAir()) {
					boolean scheduleUpdate = calculateTargetBlockAndPropagate(state, world, pos, world.getRandom());
					if (scheduleUpdate) {
						world.scheduleTick(pos, state.getBlock(), 2 + random.nextInt(5));
					}
				} else {
					Player owner = PlayerOwned.getPlayerIfOnline(world, blockFlooderBlockEntity.getOwnerUUID());
					if(owner == null) {
						return;
					}
					
					world.setBlock(pos, targetState, 3);
					if (!owner.isCreative()) {
						List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(new ItemStack(targetState.getBlock().asItem()), owner.getInventory());
						for (ItemStack remainder : remainders) {
							owner.getInventory().placeItemBackInInventory(remainder);
						}
					}
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
	
}
