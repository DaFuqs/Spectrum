package de.dafuqs.spectrum.blocks.lava_sponge;

import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class WetLavaSpongeItem extends BlockItem {
	
	public WetLavaSpongeItem(Block block, Item.Properties itemSettings) {
		super(block, itemSettings);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (world != null && entity != null) {
			// play fire sound, set player and surroundings on fire
			if (world.isClientSide) {
				RandomSource random = world.getRandom();
				if (random.nextInt(50) == 0) {
					entity.playSound(SoundEvents.FIRE_EXTINGUISH, 0.4F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.2F);
				}
			} else {
				int r = world.getRandom().nextInt(120);
				if (r < 2) {
					entity.setRemainingFireTicks(25);
				} else if (r < 3) {
					if (world.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
						RandomSource random = world.getRandom();
						int xOffset = 3 - random.nextInt(7);
						int yOffset = 1 - random.nextInt(3);
						int zOffset = 3 - random.nextInt(7);
						
						BlockPos targetPos = BlockPos.containing(entity.position()).offset(xOffset, yOffset, zOffset);
						if (BaseFireBlock.canBePlacedAt(world, targetPos, Direction.UP)) {
							world.setBlockAndUpdate(targetPos, BaseFireBlock.getState(world, targetPos));
						}
					}
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.wet_lava_sponge.tooltip"));
	}
	
}
