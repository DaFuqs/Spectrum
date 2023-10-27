package de.dafuqs.spectrum.helpers;

import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InWorldInteractionHelper {
	
	public static boolean findAndDecreaseClosestItemEntityOfItem(@NotNull ServerWorld world, Vec3d pos, Item item, int range) {
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, Box.of(pos, range, range, range));
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.getStack().isOf(item)) {
				decrementAndSpawnRemainder(itemEntity, 1);
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
				decrementAndSpawnRemainder(itemEntity, decrementCount);
				count -= decrementCount;
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
				decrementAndSpawnRemainder(itemEntity, decrementCount);
				count -= decrementCount;
				if (count == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void decrementAndSpawnRemainder(ItemEntity itemEntity, int amount) {
		ItemStack stack = itemEntity.getStack();
		ItemStack remainder = stack.getItem() instanceof EntityBucketItem ? Items.BUCKET.getDefaultStack() : stack.getRecipeRemainder(); // looking at you, Mojang
		if (!remainder.isEmpty()) {
			remainder.setCount(amount);
			ItemEntity remainderEntity = new ItemEntity(itemEntity.world, itemEntity.getPos().getX(), itemEntity.getPos().getY(), itemEntity.getPos().getZ(), remainder);
			itemEntity.world.spawnEntity(remainderEntity);
		}
		stack.decrement(amount);
	}
	
	public static void scatter(World world, double x, double y, double z, ItemVariant variant, long amount) {
		int maxStackSize = variant.getItem().getMaxCount();
		
		while (amount > 0) {
			int stackSize = (int) Math.min(maxStackSize, amount);
			ItemStack stack = variant.toStack(stackSize);
			ItemScatterer.spawn(world, x, y, z, stack);
			amount -= stackSize;
		}
	}
	
	
}
