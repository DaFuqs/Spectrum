package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemDetectorBlock extends DetectorBlock {
	
	public ItemDetectorBlock(Settings settings) {
		super(settings);
	}
	
	protected void updateState(BlockState state, World world, BlockPos pos) {
		List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, getBoxWithRadius(pos, 10), Entity::isAlive);
		
		int power;
		if (items.size() > 0) {
			int amount = 0;
			for (ItemEntity itementity : items) {
				ItemStack itemStack = itementity.getStack();
				amount += itemStack.getCount();
				if (amount >= 64) {
					break;
				}
			}
			power = Math.max(1, Math.min(amount / 4, 15));
		} else {
			power = 0;
		}
		
		if (state.get(POWER) != power) {
			world.setBlockState(pos, state.with(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
}
