package de.dafuqs.spectrum.helpers;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InWorldInteractionHelper {
	
	public static boolean findAndDecreaseClosestItemEntityOfItem(@NotNull ServerWorld world, Vec3d pos, Item item, int range) {
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, Box.of(pos, range, range, range));
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getStack();
			if (stack.isOf(item)) {
				stack.decrement(1);
				if (stack.isEmpty()) {
					itemEntity.discard();
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean findAndDecreaseClosestItemEntityOfItem(@NotNull World world, Vec3d pos, TagKey<Item> tag, int range, int count) {
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, Box.of(pos, range, range, range));
		int foundCount = 0;
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getStack();
			if (stack.isIn(tag)) {
				foundCount += stack.getCount();
				if (foundCount >= count) {
					break;
				}
			}
		}
		
		if (foundCount < count) {
			return false;
		}
		
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getStack();
			if (stack.isIn(tag)) {
				int decrementCount = Math.min(stack.getCount(), count);
				stack.decrement(decrementCount);
				count -= decrementCount;
				if (stack.isEmpty()) {
					itemEntity.discard();
				}
				if (count == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean findAndDecreaseClosestItemEntityOfItem(@NotNull World world, Vec3d pos, Item item, int range, int count) {
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, Box.of(pos, range, range, range));
		int foundCount = 0;
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getStack();
			if (stack.isOf(item)) {
				foundCount += stack.getCount();
				if (foundCount >= count) {
					break;
				}
			}
		}
		
		if (foundCount < count) {
			return false;
		}
		
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getStack();
			if (stack.isOf(item)) {
				int decrementCount = Math.min(stack.getCount(), count);
				stack.decrement(decrementCount);
				count -= decrementCount;
				if (stack.isEmpty()) {
					itemEntity.discard();
				}
				if (count == 0) {
					return true;
				}
			}
		}
		return false;
	}
}
