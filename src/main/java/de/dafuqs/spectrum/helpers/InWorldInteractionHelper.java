package de.dafuqs.spectrum.helpers;

import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InWorldInteractionHelper {
	
	public static boolean findAndDecreaseClosestItemEntityOfItem(@NotNull ServerLevel world, Vec3 pos, Item item, int range) {
		List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pos, range, range, range));
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.getItem().is(item)) {
				decrementAndSpawnRemainder(itemEntity, 1);
				return true;
			}
		}
		return false;
	}
	
	public static boolean findAndDecreaseClosestItemEntityOfItem(@NotNull Level world, Vec3 pos, TagKey<Item> tag, int range, int count) {
		List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pos, range, range, range));
		int foundCount = 0;
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getItem();
			if (stack.is(tag)) {
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
			ItemStack stack = itemEntity.getItem();
			if (stack.is(tag)) {
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
	
	public static boolean findAndDecreaseClosestItemEntityOfItem(@NotNull Level world, Vec3 pos, Item item, int range, int count) {
		List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pos, range, range, range));
		int foundCount = 0;
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getItem();
			if (stack.is(item)) {
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
			ItemStack stack = itemEntity.getItem();
			if (stack.is(item)) {
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
		ItemStack stack = itemEntity.getItem();
		ItemStack remainder = stack.getItem() instanceof MobBucketItem ? Items.BUCKET.getDefaultInstance() : stack.getRecipeRemainder(); // looking at you, Mojang
		if (!remainder.isEmpty()) {
			remainder.setCount(amount);
			ItemEntity remainderEntity = new ItemEntity(itemEntity.level(), itemEntity.position().x(), itemEntity.position().y(), itemEntity.position().z(), remainder);
			itemEntity.level().addFreshEntity(remainderEntity);
		}
		stack.shrink(amount);
	}
	
	public static void scatter(Level world, double x, double y, double z, ItemVariant variant, long amount) {
		int maxStackSize = variant.getItem().getDefaultMaxStackSize();

		while (amount > 0) {
			int stackSize = (int) Math.min(maxStackSize, amount);
			ItemStack stack = variant.toStack(stackSize);
			Containers.dropItemStack(world, x, y, z, stack);
			amount -= stackSize;
		}
	}


}
