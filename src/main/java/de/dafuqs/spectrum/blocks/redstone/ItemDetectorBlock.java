package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ItemDetectorBlock extends DetectorBlock {
	
	public static final MapCodec<ItemDetectorBlock> CODEC = simpleCodec(ItemDetectorBlock::new);
	
	public ItemDetectorBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends ItemDetectorBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void updateState(BlockState state, Level world, BlockPos pos) {
		List<ItemEntity> items = world.getEntities(EntityType.ITEM, getDetectionBox(pos), Entity::isAlive);
		
		int power;
		if (items.size() > 0) {
			int amount = 0;
			for (ItemEntity itementity : items) {
				ItemStack itemStack = itementity.getItem();
				amount += itemStack.getCount();
				if (amount >= 64) {
					break;
				}
			}
			power = Math.max(1, Math.min(amount / 4, 15));
		} else {
			power = 0;
		}
		
		power = state.getValue(INVERTED) ? 15 - power : power;
		if (state.getValue(POWER) != power) {
			world.setBlock(pos, state.setValue(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
}
