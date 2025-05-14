package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.api.interaction.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;

public class BuildingHelper {
	
	private static final Map<TagKey<Block>, List<Block>> SIMILAR_BLOCKS = new HashMap<>() {{
		put(BlockTags.DIRT, new ArrayList<>() {{
			add(Blocks.GRASS_BLOCK);
		}});
		put(BlockTags.NYLIUM, new ArrayList<>() {{
			add(Blocks.NETHERRACK);
		}});
	}};

	private static final Map<Block, List<Block>> SIMILAR_BLOCKS_CACHE = new HashMap<>();
	
	private static final ArrayList<Vec3i> NEIGHBOR_VECTORS_Y = new ArrayList<>() {{
		add(Direction.NORTH.getNormal());
		add(Direction.EAST.getNormal());
		add(Direction.SOUTH.getNormal());
		add(Direction.WEST.getNormal());
		add(Direction.WEST.getNormal().relative(Direction.NORTH));
		add(Direction.NORTH.getNormal().relative(Direction.EAST));
		add(Direction.EAST.getNormal().relative(Direction.SOUTH));
		add(Direction.SOUTH.getNormal().relative(Direction.WEST));
	}};
	
	public static Triplet<Block, Item, Integer> getBuildingItemCountInInventoryIncludingSimilars(Player player, Block block, long maxCount) {
		Item blockItem = block.asItem();
		if (blockItem instanceof ItemNameBlockItem aliasedBlockItem) {
			// do not process seeds and similar stuff
			// otherwise players could place fully grown crops
			return new Triplet<>(block, aliasedBlockItem, 0);
		} else {
			for (Block similarBlock : getSimilarBlocks(block)) {
				Item similarBlockItem = similarBlock.asItem();
				Inventory playerInventory = player.getInventory();
				int similarCount = playerInventory.countItem(similarBlockItem);
				for (int i = 0; i < playerInventory.getContainerSize(); i++) {
					ItemStack currentStack = playerInventory.getItem(i);
					
					ItemProvider itemProvider = ItemProviderRegistry.getProvider(currentStack);
					if (itemProvider != null) {
						similarCount += itemProvider.getItemCount(player, currentStack, similarBlockItem);
					}
				}
				if (similarCount > 0) {
					return new Triplet<>(similarBlock, similarBlockItem, (int) Math.min(similarCount, maxCount));
				}
			}
			return new Triplet<>(block, blockItem, 0);
		}
	}

	/**
	 * A simple implementation of a breadth first search
	 */
	public static @NotNull List<BlockPos> getConnectedBlocks(@NotNull Level world, @NotNull BlockPos blockPos, long maxCount, int maxRange) {
		BlockState originState = world.getBlockState(blockPos);
		Block originBlock = originState.getBlock();

		ArrayList<BlockPos> connectedPositions = new ArrayList<>();
		ArrayList<BlockPos> visitedPositions = new ArrayList<>();
		Queue<BlockPos> positionsToVisit = new LinkedList<>();

		connectedPositions.add(blockPos);
		visitedPositions.add(blockPos);
		positionsToVisit.add(blockPos);
		while (connectedPositions.size() < maxCount) {
			BlockPos currentPos = positionsToVisit.poll();
			if (currentPos == null) {
				break;
			} else {
				for (Direction direction : Direction.values()) {
					BlockPos offsetPos = currentPos.relative(direction);
					if (!visitedPositions.contains(offsetPos)) {
						visitedPositions.add(offsetPos);
						if (blockPos.closerThan(offsetPos, maxRange)) {
							Block localBlock = world.getBlockState(offsetPos).getBlock();
							if (getSimilarBlocks(localBlock).contains(originBlock)) {
								positionsToVisit.add(offsetPos);
								connectedPositions.add(offsetPos);
								if (connectedPositions.size() >= maxCount) {
									break;
								}
							}
						}
					}
				}
			}
		}

		return connectedPositions;
	}
	
	public static @NotNull List<BlockPos> calculateBuildingStaffSelection(@NotNull Level world, @NotNull BlockPos originPos, Direction direction, long maxCount, int maxRange, boolean sameBlockOnly) {
		BlockPos offsetPos = originPos.relative(direction);
		BlockState originState = world.getBlockState(originPos);
		
		List<BlockPos> selectedPositions = new ArrayList<>();
		int count = 1;
		
		List<BlockPos> storedNeighbors = new ArrayList<>();
		if (world.isUnobstructed(originState, offsetPos, CollisionContext.empty())) {
			storedNeighbors.add(offsetPos);
		}
		
		while (count < maxCount && !storedNeighbors.isEmpty()) {
			selectedPositions.addAll(storedNeighbors);
			List<BlockPos> newNeighbors = new ArrayList<>();
			
			for (BlockPos neighbor : storedNeighbors) {
				List<BlockPos> facingNeighbors = getValidNeighbors(world, neighbor, direction, originState,
						sameBlockOnly);

				for (BlockPos facingNeighbor : facingNeighbors) {
					if (count < maxCount && originPos.closerThan(facingNeighbor, maxRange)) {
						if (!selectedPositions.contains(facingNeighbor) && !storedNeighbors.contains(facingNeighbor)
								&& !newNeighbors.contains(facingNeighbor)) {
							newNeighbors.add(facingNeighbor);
							count++;
						}
					}
				}
			}
			storedNeighbors.clear();
			storedNeighbors.addAll(newNeighbors);
		}
		selectedPositions.addAll(storedNeighbors);
		return selectedPositions;
	}
	
	private static @NotNull List<BlockPos> getValidNeighbors(Level world, BlockPos startPos, Direction facingDirection, BlockState originState, boolean similarBlockOnly) {
		List<BlockPos> foundNeighbors = new ArrayList<>();
		for (Vec3i neighborVectors : getNeighborVectors(facingDirection)) {
			BlockPos targetPos = startPos.offset(neighborVectors);
			BlockState targetState = world.getBlockState(targetPos);
			BlockState facingAgainstState = world.getBlockState(targetPos.relative(facingDirection.getOpposite()));
			
			if ((targetState.canBeReplaced() || !targetState.getFluidState().isEmpty())
					&& world.isUnobstructed(originState, targetPos, CollisionContext.empty())) {
				if (similarBlockOnly) {
					if (getSimilarBlocks(facingAgainstState.getBlock()).contains(originState.getBlock())) {
						foundNeighbors.add(targetPos);
					}
				} else {
					if (!facingAgainstState.isAir()) {
						foundNeighbors.add(targetPos);
					}
				}
			}
		}
		
		return foundNeighbors;
	}
	
	private static @NotNull List<Vec3i> getNeighborVectors(@NotNull Direction direction) {
		if (direction.getAxis() == Direction.Axis.Y) {
			return NEIGHBOR_VECTORS_Y;
		} else {
			return new ArrayList<>() {
				{
					add(direction.getClockWise().getNormal());
					add(direction.getCounterClockWise().getNormal());
					add(Direction.UP.getNormal());
					add(Direction.DOWN.getNormal());
				}
			};
		}
	}
	
	private static List<Block> getSimilarBlocks(Block block) {
		List<Block> similarBlocks = SIMILAR_BLOCKS_CACHE.get(block);
		if (similarBlocks == null) {
			similarBlocks = new ArrayList<>() {
				{
					add(block);
					for (Map.Entry<TagKey<Block>, List<Block>> entry : SIMILAR_BLOCKS.entrySet()) {
						if (block.defaultBlockState().is(entry.getKey())) {
							addAll(entry.getValue());
						}
					}
				}
			};
			SIMILAR_BLOCKS_CACHE.put(block, similarBlocks);
		}
		return similarBlocks;
	}
}