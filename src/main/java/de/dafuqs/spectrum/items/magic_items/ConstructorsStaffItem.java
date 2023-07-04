package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.helpers.*;
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

public class ConstructorsStaffItem extends BuildingStaffItem implements InkPowered {
	
	public static final InkColor USED_COLOR = InkColors.CYAN;
	public static final int INK_COST_PER_BLOCK = 1;
	public static final int CREATIVE_RANGE = 10;
	
	public ConstructorsStaffItem(Settings settings) {
		super(settings);
	}
	
	// The range grows with the players' progression
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
		super.appendTooltip(stack, world, tooltip, context);
		addInkPoweredTooltip(tooltip);
		tooltip.add(Text.translatable("item.spectrum.constructors_staff.tooltip.range", getRange(MinecraftClient.getInstance().player)).formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.constructors_staff.tooltip.crouch").formatted(Formatting.GRAY));
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState targetBlockState = world.getBlockState(pos);

		if ((player != null && (player.isCreative()) || canProcess(targetBlockState, context.getWorld(), context.getBlockPos(), context.getPlayer()))) {
			Block targetBlock = targetBlockState.getBlock();
			Item targetBlockItem = targetBlock.asItem();

			if (player != null && targetBlockItem != Items.AIR) {
				long count;
				if (player.isCreative()) {
					count = Integer.MAX_VALUE;
				} else {
					Triplet<Block, Item, Integer> inventoryItemAndCount = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, targetBlock);
					if (targetBlock != inventoryItemAndCount.getA()) {
						targetBlockState = inventoryItemAndCount.getA().getDefaultState();
					}
					targetBlockItem = inventoryItemAndCount.getB();
					count = inventoryItemAndCount.getC();
					
					if (InkPowered.canUse(player)) {
						count = Math.min(count, 1 + InkPowered.getAvailableInk(player, USED_COLOR) / INK_COST_PER_BLOCK);
					} else {
						count = 0;
					}
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
					
					int placed = 0;
					if (!world.isClient) {
						for (BlockPos position : targetPositions) {
							BlockState originalState = world.getBlockState(position);
							if (originalState.isAir() || !originalState.getFluidState().isEmpty() || (originalState.getMaterial().isReplaceable() && originalState.getCollisionShape(world, position).isEmpty())) {
								BlockState stateToPlace = targetBlock.getPlacementState(new BuildingStaffPlacementContext(world, player, new BlockHitResult(Vec3d.ofBottomCenter(pos), side, pos, false)));
								if (stateToPlace != null && stateToPlace.canPlaceAt(world, pos)) {
									if (world.setBlockState(position, stateToPlace)) {
										placed++;
									}
								}
							}
						}
						
						if (!player.isCreative()) {
							Item finalTargetBlockItem = targetBlockItem;
							player.getInventory().remove(stack -> stack.getItem().equals(finalTargetBlockItem), placed, player.getInventory());
							InkPowered.tryDrainEnergy(player, USED_COLOR, (long) targetPositions.size() * INK_COST_PER_BLOCK);
						}
						
						if (placed > 0) {
							world.playSound(null, player.getBlockPos(), targetBlockState.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, targetBlockState.getSoundGroup().getVolume(), targetBlockState.getSoundGroup().getPitch());
						}
					}
					
					return ActionResult.SUCCESS;
				}
			}
		} else {
			if (player != null) {
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
		
		return ActionResult.FAIL;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}
	
}
