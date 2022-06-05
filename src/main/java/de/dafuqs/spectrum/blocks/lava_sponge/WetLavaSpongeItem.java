package de.dafuqs.spectrum.blocks.lava_sponge;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class WetLavaSpongeItem extends BlockItem {
	
	public WetLavaSpongeItem(Block block, FabricItemSettings fabricItemSettings) {
		super(block, fabricItemSettings);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world != null && entity != null) {
			// play fire sound, set player and surroundings on fire
			if (world.isClient) {
				Random random = world.getRandom();
				if (random.nextInt(50) == 0) {
					entity.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.4F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.2F);
				}
			} else {
				int r = world.getRandom().nextInt(120);
				if (r < 2) {
					entity.setFireTicks(25);
				} else if (r < 3) {
					if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
						Random random = world.getRandom();
						int xOffset = 3 - random.nextInt(7);
						int yOffset = 1 - random.nextInt(3);
						int zOffset = 3 - random.nextInt(7);
						
						BlockPos targetPos = new BlockPos(entity.getPos()).add(xOffset, yOffset, zOffset);
						if (world.getBlockState(targetPos).isAir() && world.getBlockState(targetPos.down()).getMaterial().isSolid()) {
							world.setBlockState(targetPos, Blocks.FIRE.getDefaultState());
						}
					}
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(new TranslatableText("item.spectrum.wet_lava_sponge.tooltip"));
	}
	
}
