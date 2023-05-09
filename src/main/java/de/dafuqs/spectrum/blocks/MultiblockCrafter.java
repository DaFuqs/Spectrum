package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.interfaces.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public interface MultiblockCrafter extends Upgradeable, PlayerOwned {
	
	Vec3d RECIPE_STACK_VELOCITY = new Vec3d(0.0, 0.3, 0.0);
	
	static @Nullable Recipe getRecipeFromId(@Nullable World world, Identifier recipeIdentifier) {
		if (world != null) {
			return world.getRecipeManager().get(recipeIdentifier).orElse(null);
		}
		if (SpectrumCommon.minecraftServer != null) {
			return SpectrumCommon.minecraftServer.getRecipeManager().get(recipeIdentifier).orElse(null);
		}
		return null;
	}
	
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
