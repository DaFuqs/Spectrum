package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiblockCrafter extends Upgradeable, PlayerOwned {
	
	static void spawnExperience(World world, BlockPos blockPos, int spawnedXPAmount) {
		if (spawnedXPAmount > 0) {
			ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, spawnedXPAmount);
			world.spawnEntity(experienceOrbEntity);
		}
	}
	
	static void spawnItemStackAsEntitySplitViaMaxCount(World world, BlockPos blockPos, ItemStack itemStack, int amount) {
		while (amount > 0) {
			int currentAmount = Math.min(amount, itemStack.getMaxCount());
			
			ItemStack resultStack = itemStack.copy();
			resultStack.setCount(currentAmount);
			ItemEntity itemEntity = new ItemEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, resultStack);
			itemEntity.setVelocity(0, 0.3, 0);
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
