package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface MultiblockCrafter extends Upgradeable, PlayerOwned {
	
	Vec3d RECIPE_STACK_VELOCITY = new Vec3d(0.0, 0.3, 0.0);

	static <T extends Recipe<?>> @Nullable T getRecipeFromNbt(@Nullable World world, NbtCompound nbt, Class<T> recipeClass) {
		if (nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty()) {
				Optional<RecipeManager> recipeManager = SpectrumCommon.getRecipeManager(world);
				if (recipeManager.isPresent()) {
					Recipe<?> r = recipeManager.get().get(new Identifier(recipeString)).get();
					if (recipeClass.isInstance(r)) {
						return (T) r;
					}
				}
			}
		}
		return null;
	}
	
	static void spawnExperience(World world, BlockPos blockPos, float amount, Random random) {
		spawnExperience(world, blockPos, Support.getIntFromDecimalWithChance(amount, random));
	}
	
	static void spawnExperience(World world, BlockPos blockPos, int amount) {
		if (amount > 0) {
			ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, amount);
			world.spawnEntity(experienceOrbEntity);
		}
	}
	
	static void spawnItemStackAsEntitySplitViaMaxCount(World world, BlockPos blockPos, ItemStack itemStack, int amount, Vec3d velocity) {
		spawnItemStackAsEntitySplitViaMaxCount(world, Vec3d.ofCenter(blockPos), itemStack, amount, velocity, true, null);
	}
	
	static void spawnItemStackAsEntitySplitViaMaxCount(World world, Vec3d pos, ItemStack itemStack, int amount, Vec3d velocity, boolean neverDespawn, @Nullable Entity owner) {
		while (amount > 0) {
			int currentAmount = Math.min(amount, itemStack.getMaxCount());
			
			ItemStack resultStack = itemStack.copy();
			resultStack.setCount(currentAmount);
			ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), resultStack);
			itemEntity.setVelocity(velocity);
			if (neverDespawn) {
				itemEntity.setNeverDespawn();
			}
			if (owner != null) {
				itemEntity.setOwner(owner.getUuid());
			}
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
