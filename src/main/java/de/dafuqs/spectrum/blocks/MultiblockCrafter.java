package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface MultiblockCrafter extends Upgradeable, PlayerOwned {
	
	Vec3d RECIPE_STACK_VELOCITY = new Vec3d(0.0, 0.3, 0.0);
	
	static void spawnExperience(World world, BlockPos blockPos, int spawnedXPAmount) {
		if (spawnedXPAmount > 0) {
			ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, spawnedXPAmount);
			world.spawnEntity(experienceOrbEntity);
		}
	}
	
	static void spawnItemStackAsEntitySplitViaMaxCount(World world, BlockPos blockPos, ItemStack itemStack, int amount, Vec3d velocity) {
		spawnItemStackAsEntitySplitViaMaxCount(world, Vec3d.ofCenter(blockPos), itemStack, amount, velocity);
	}
	
	static void spawnItemStackAsEntitySplitViaMaxCount(World world, Vec3d pos, ItemStack itemStack, int amount, Vec3d velocity) {
		while (amount > 0) {
			int currentAmount = Math.min(amount, itemStack.getMaxCount());
			
			ItemStack resultStack = itemStack.copy();
			resultStack.setCount(currentAmount);
			ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), resultStack);
			itemEntity.setVelocity(velocity);
			world.spawnEntity(itemEntity);
			
			amount -= currentAmount;
		}
	}
	
	static void spawnOutputAsItemEntity(World world, BlockPos pos, ItemStack outputItemStack) {
		ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, outputItemStack);
		itemEntity.addVelocity(0, 0.1, 0);
		world.spawnEntity(itemEntity);
	}
	
}
