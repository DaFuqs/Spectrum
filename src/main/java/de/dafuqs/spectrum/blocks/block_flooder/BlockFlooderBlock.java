package de.dafuqs.spectrum.blocks.block_flooder;

import com.google.common.collect.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockFlooderBlock extends BlockWithEntity {
	
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
	public final BlockState DEFAULT_BLOCK_STATE = Blocks.COBBLESTONE.getDefaultState();
	
	public BlockFlooderBlock(Settings settings) {
		super(settings);
	}
	
	public static boolean isReplaceableBlock(World world, BlockPos blockPos) {
		BlockState state = world.getBlockState(blockPos);
		Block block = state.getBlock();
		return world.getBlockEntity(blockPos) == null && !(block instanceof BlockFlooderBlock) && (state.isAir() || block instanceof FluidBlock || state.getMaterial().isReplaceable() || block instanceof AbstractPlantBlock || block instanceof FlowerBlock);
	}
	
	public static boolean isValidCornerBlock(World world, BlockPos blockPos) {
		BlockState state = world.getBlockState(blockPos);
		Block block = state.getBlock();
		return state.isSolidBlock(world, blockPos) || block instanceof FluidBlock || block instanceof BlockFlooderBlock;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient) {
			world.createAndScheduleBlockTick(pos, state.getBlock(), 4);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BlockFlooderBlockEntity(pos, state);
	}
	
	@SuppressWarnings("deprecation")
	private boolean calculateTargetBlockAndPropagate(BlockState state, World world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
			PlayerEntity owner = PlayerOwned.getPlayerEntityIfOnline(blockFlooderBlockEntity.getOwnerUUID());
			if (owner == null) {
				world.setBlockState(pos, DEFAULT_BLOCK_STATE, 3);
				return false;
			}
			
			Map<Block, Integer> neighboringBlockAmounts = new HashMap<>();
			for (Direction direction : Direction.values()) {
				BlockPos targetBlockPos = pos.offset(direction);
				BlockState currentBlockState = world.getBlockState(targetBlockPos);
				BlockEntity currentBlockEntity = world.getBlockEntity(targetBlockPos);
				
				if (!currentBlockState.isOf(this) && currentBlockEntity == null) {
					if (isReplaceableBlock(world, targetBlockPos)) {
						Vec3i nextPos = new Vec3i(targetBlockPos.offset(direction).getX(), targetBlockPos.offset(direction).getY(), targetBlockPos.offset(direction).getZ());
						if (blockFlooderBlockEntity.getSourcePos().isWithinDistance(nextPos, MAX_DISTANCE)
								&& GenericClaimModsCompat.canPlaceBlock(world, targetBlockPos, owner)
								&& shouldPropagateTo(world, targetBlockPos)) {
							
							world.setBlockState(targetBlockPos, state, 3);
							if (world.getBlockEntity(targetBlockPos) instanceof BlockFlooderBlockEntity neighboringBlockFlooderBlockEntity) {
								neighboringBlockFlooderBlockEntity.setOwnerUUID(blockFlooderBlockEntity.getOwnerUUID());
								neighboringBlockFlooderBlockEntity.setSourcePos(blockFlooderBlockEntity.getSourcePos());
							}
						}
					} else {
						Block currentBlock = currentBlockState.getBlock();
						
						if (currentBlockState.isSolidBlock(world, targetBlockPos)) {
							if (neighboringBlockAmounts.containsKey(currentBlock)) {
								neighboringBlockAmounts.put(currentBlock, neighboringBlockAmounts.get(currentBlock) + 1);
							} else {
								neighboringBlockAmounts.put(currentBlock, 1);
							}
						}
					}
				}
			}
			
			if (neighboringBlockAmounts.size() > 0) {
				int max = 0;
				Block maxBlock = null;
				
				for (Map.Entry<Block, Integer> entry : neighboringBlockAmounts.entrySet()) {
					Block currentBlock = entry.getKey();
					int currentOccurrences = entry.getValue();
					Item blockItem = currentBlock.asItem();
					
					if (blockItem != Items.AIR) {
						if (currentOccurrences > max || (currentOccurrences == max && random.nextBoolean())) {
							ItemStack currentItemStack = new ItemStack(blockItem);
							if (owner.isCreative() || owner.getInventory().contains(currentItemStack) && currentBlock.canPlaceAt(currentBlock.getDefaultState(), world, pos)) {
								maxBlock = currentBlock;
							} else {
								Optional<TagKey<Block>> tag = Support.getFirstMatchingBlockTag(currentBlock.getDefaultState(), exchangeBlockTags);
								if (tag.isPresent()) {
									currentBlock = exchangeableBlocks.get(tag.get());
									blockItem = currentBlock.asItem();
									if (blockItem != Items.AIR) {
										currentItemStack = new ItemStack(blockItem);
										if (owner.isCreative() || owner.getInventory().contains(currentItemStack) && currentBlock.canPlaceAt(currentBlock.getDefaultState(), world, pos)) {
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
					blockFlooderBlockEntity.setTargetBlockState(maxBlock.getDefaultState());
				} else {
					blockFlooderBlockEntity.setTargetBlockState(DEFAULT_BLOCK_STATE);
				}
			}
		}
		return true;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		
		if (!world.isClient) {
			if (world.getBlockEntity(pos) instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
				BlockState targetState = blockFlooderBlockEntity.getTargetBlockState();
				if (targetState == null || targetState.isAir()) {
					boolean scheduleUpdate = calculateTargetBlockAndPropagate(state, world, pos, world.getRandom());
					if (scheduleUpdate) {
						world.createAndScheduleBlockTick(pos, state.getBlock(), 2 + random.nextInt(5));
					}
				} else {
					world.setBlockState(pos, targetState, 3);
					PlayerEntity owner = PlayerOwned.getPlayerEntityIfOnline(blockFlooderBlockEntity.getOwnerUUID());
					if (!owner.isCreative()) {
						List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(new ItemStack(targetState.getBlock().asItem()), owner.getInventory());
						for (ItemStack remainder : remainders) {
							owner.getInventory().offerOrDrop(remainder);
						}
					}
				}
			}
		}
		
	}
	
	private boolean shouldPropagateTo(World world, BlockPos targetBlockPos) {
		if (isReplaceableBlock(world, targetBlockPos)) {
			int count = 0;
			for (Direction direction : Direction.values()) {
				for (int i = 1; i < MAX_DISTANCE / 2; i++) {
					BlockPos offsetPos = targetBlockPos.offset(direction, i);
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
