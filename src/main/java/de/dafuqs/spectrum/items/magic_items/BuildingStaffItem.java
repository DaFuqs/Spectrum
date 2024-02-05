package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;

public abstract class BuildingStaffItem extends Item implements PrioritizedBlockInteraction, InkPowered {
	
	public static final InkColor USED_COLOR = InkColors.CYAN;
	
	public BuildingStaffItem(Settings settings) {
		super(settings);
	}
	
	public boolean canInteractWith(BlockState state, BlockView world, BlockPos pos, PlayerEntity player) {
		if (state.getBlock().asItem() == Items.AIR) {
			return false;
		}
		if (player == null || world.getBlockEntity(pos) != null || state.isIn(SpectrumBlockTags.BUILDING_STAFFS_BLACKLISTED)) {
			return false;
		}
		if (player.isCreative()) {
			return true;
		}
		
		float hardness = state.getHardness(world, pos);
		return hardness >= 0 && GenericClaimModsCompat.canInteractWith(player.getWorld(), pos, player);
	}
	
	/**
	 * @return The block to place, the blockItem to consume, the amount
	 */
	protected static Triplet<Block, Item, Integer> countSuitableReplacementItems(@NotNull PlayerEntity player, @NotNull Block targetBlock, boolean single, int inkCostPerBlock) {
		if (player.isCreative()) {
			return new Triplet<>(targetBlock, targetBlock.asItem(), single ? 1 : Integer.MAX_VALUE);
		}
		
		long blocksToPlace;
		if (single) {
			blocksToPlace = InkPowered.getAvailableInk(player, USED_COLOR) >= inkCostPerBlock ? 1 : 0;
		} else {
			blocksToPlace = InkPowered.getAvailableInk(player, USED_COLOR) / inkCostPerBlock;
		}
		blocksToPlace = Math.min(1024, blocksToPlace); // to not yeet performance out the window
		
		return BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, targetBlock, blocksToPlace);
	}

	// TODO - Refactor note - Is PlacedBlocks planned on being used? At the same time, should this specific impl not be in ConstructorStaffItem?
	protected static int placeBlocksAndDecrementInventory(PlayerEntity player, World world, Block blockToPlace, Item itemToConsume, Direction side, List<BlockPos> targetPositions, int inkCostPerBlock) {
		int placedBlocks = 0;
		for (BlockPos position : targetPositions) {
			// Only place blocks where you are allowed to do so
			if (!GenericClaimModsCompat.canPlaceBlock(world, position, player))
				continue;

			BlockState originalState = world.getBlockState(position);
			if (originalState.isAir() || originalState.getBlock() instanceof FluidBlock || (originalState.getMaterial().isReplaceable() && originalState.getCollisionShape(world, position).isEmpty())) {
				BlockState stateToPlace = blockToPlace.getPlacementState(new BuildingStaffPlacementContext(world, player, new BlockHitResult(Vec3d.ofBottomCenter(position), side, position, false)));
				if (stateToPlace != null && stateToPlace.canPlaceAt(world, position)) {
					if (world.setBlockState(position, stateToPlace)) {
						if (placedBlocks == 0) {
							world.playSound(null, player.getBlockPos(), stateToPlace.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, stateToPlace.getSoundGroup().getVolume(), stateToPlace.getSoundGroup().getPitch());
						}
						placedBlocks++;
					}
				}
			}
		}
		
		if (!player.isCreative()) {
			player.getInventory().remove(stack -> stack.getItem().equals(itemToConsume), placedBlocks, player.getInventory());
			InkPowered.tryDrainEnergy(player, USED_COLOR, (long) targetPositions.size() * inkCostPerBlock);
		}
		
		return placedBlocks;
	}
	
	public static class BuildingStaffPlacementContext extends ItemPlacementContext {
		
		public BuildingStaffPlacementContext(World world, @Nullable PlayerEntity playerEntity, BlockHitResult blockHitResult) {
			super(world, playerEntity, Hand.MAIN_HAND, ItemStack.EMPTY, blockHitResult);
		}
		
	}
	
}
