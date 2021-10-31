package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.BuildingHelper;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class PlacementStaffItem extends Item {
	
	public static final int CREATIVE_RANGE = 10;
	
	public PlacementStaffItem(Settings settings) {
		super(settings);
	}

	// The range grows with the players' progression
	// this way the item is not overpowered at the start
	// but not useless at the end
	// this way the player does not need to craft 5 tiers
	// of placementStaffs that each do basically feel the same
	public int getRange(PlayerEntity playerEntity) {
		if(playerEntity == null || playerEntity.isCreative()) {
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
		tooltip.add(new TranslatableText("item.spectrum.placement_staff.tooltip.range", getRange(MinecraftClient.getInstance().player)).formatted(Formatting.GRAY));
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();
		
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		
		BlockState targetBlockState = world.getBlockState(pos);
		Block targetBlock = targetBlockState.getBlock();
		
		if(!SpectrumBlockTags.PLACEMENT_STAFF_BLACKLISTED.contains(targetBlock)) {
			Item targetBlockItem = targetBlock.asItem();
			
			if (player != null && targetBlockItem != Items.AIR && context.getHand() == Hand.MAIN_HAND) {
				int count = player.getInventory().count(targetBlockItem);
				
				if (count > 0 || player.isCreative()) {
					Direction side = context.getSide();
					
					int range = Math.min(getRange(player), player.isCreative() ? getRange(player) : count);
					boolean sneaking = player.isSneaking();
					List<BlockPos> targetPositions = BuildingHelper.calculateSelection(world, pos, side, count, range, !sneaking);
					if (targetPositions.isEmpty()) {
						return ActionResult.FAIL;
					}
					
					int taken = 0;
					if (!world.isClient) {
						for (BlockPos position : targetPositions) {
							BlockState originalState = world.getBlockState(position);
							if (originalState.isAir() || !originalState.getFluidState().isEmpty() || (originalState.getMaterial().isReplaceable() && originalState.getCollisionShape(world, position).isEmpty())) {
								world.setBlockState(position, targetBlockState);
								taken++;
							}
						}
						
						if (!player.isCreative()) {
							player.getInventory().remove(stack -> stack.getItem().equals(targetBlockItem), taken, player.getInventory());
						}
						
						if (taken > 0) {
							world.playSound(null, player.getBlockPos(), targetBlockState.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, targetBlockState.getSoundGroup().getVolume(), targetBlockState.getSoundGroup().getPitch());
						}
					}
					
					return ActionResult.SUCCESS;
				}
			}
		}
		
		return ActionResult.FAIL;
	}
	
	
}
