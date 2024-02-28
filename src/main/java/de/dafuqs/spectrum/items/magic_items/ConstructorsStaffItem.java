package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import oshi.util.tuples.*;

import java.util.*;

public class ConstructorsStaffItem extends BuildingStaffItem {

	public static final int INK_COST_PER_BLOCK = 1;
	public static final int CREATIVE_RANGE = 10;
	
	public ConstructorsStaffItem(Settings settings) {
		super(settings);
	}
	
	// The range grows with the players progression
	// this way the item is not overpowered at the start
	// but not useless at the end
	// this way the player does not need to craft 5 tiers
	// of staffs that each do basically feel the same
	public static int getRange(PlayerEntity playerEntity) {
		if (playerEntity == null || playerEntity.isCreative()) {
			return CREATIVE_RANGE;
		} else {
			Optional<PedestalRecipeTier> highestUnlockedRecipeTier = PedestalRecipeTier.getHighestUnlockedRecipeTier(playerEntity);
			if (highestUnlockedRecipeTier.isPresent()) {
				switch (highestUnlockedRecipeTier.get()) {
					case COMPLEX -> {
						return 10;
					}
					case ADVANCED -> {
						return 7;
					}
					default -> {
						return 4;
					}
				}
			} else {
				return 3;
			}
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		super.appendTooltip(stack, world, tooltip, context);
		addInkPoweredTooltip(tooltip);
		tooltip.add(Text.translatable("item.spectrum.constructors_staff.tooltip.range", getRange(client.player)).formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.constructors_staff.tooltip.crouch").formatted(Formatting.GRAY));
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState targetBlockState = world.getBlockState(pos);

		if ((player != null && this.canInteractWith(targetBlockState, context.getWorld(), context.getBlockPos(), context.getPlayer()))) {
			Block blockToPlace = targetBlockState.getBlock();
			Item itemToConsume;

			long count;
			if (player.isCreative()) {
				itemToConsume = blockToPlace.asItem();
				count = Integer.MAX_VALUE;
			} else {
				Triplet<Block, Item, Integer> replaceData = countSuitableReplacementItems(player, blockToPlace, false, INK_COST_PER_BLOCK);
				blockToPlace = replaceData.getA();
				itemToConsume = replaceData.getB();
				count = replaceData.getC();
			}

			if (count > 0) {
				Direction side = context.getSide();
				int maxRange = getRange(player);
				int range = (int) Math.min(maxRange, player.isCreative() ? maxRange : count);
				boolean sneaking = player.isSneaking();
				List<BlockPos> targetPositions = BuildingHelper.calculateBuildingStaffSelection(world, pos, side, count, range, !sneaking);
				if (targetPositions.isEmpty()) {
					return ActionResult.FAIL;
				}

				if (!world.isClient) {
					placeBlocksAndDecrementInventory(player, world, blockToPlace, itemToConsume, side, targetPositions);
				}

				return ActionResult.SUCCESS;
			}
		} else {
			if (player != null) {
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
		
		return ActionResult.FAIL;
	}
	
	protected static void placeBlocksAndDecrementInventory(PlayerEntity player, World world, Block blockToPlace, Item itemToConsume, Direction side, List<BlockPos> targetPositions) {
		int placedBlocks = 0;
		for (BlockPos position : targetPositions) {
			// Only place blocks where you are allowed to do so
			if (!GenericClaimModsCompat.canPlaceBlock(world, position, player))
				continue;
			
			BlockState originalState = world.getBlockState(position);
			if (originalState.isAir() || originalState.getBlock() instanceof FluidBlock || (originalState.isReplaceable() && originalState.getCollisionShape(world, position).isEmpty())) {
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
			InkPowered.tryDrainEnergy(player, USED_COLOR, (long) targetPositions.size() * ConstructorsStaffItem.INK_COST_PER_BLOCK);
		}
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}
	
}
