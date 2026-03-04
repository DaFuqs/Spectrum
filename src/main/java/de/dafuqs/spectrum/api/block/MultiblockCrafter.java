package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public interface MultiblockCrafter extends Upgradeable, PlayerOwned {
	
	Vec3 RECIPE_STACK_VELOCITY = new Vec3(0.0, 0.3, 0.0);
	
	static @Nullable RecipeHolder<?> getRecipeHolderFromNbt(@Nullable Level world, CompoundTag nbt) {
		if (nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty()) {
				return SpectrumCommon.getRecipeManager(world).flatMap(m -> m.byKey(ResourceLocation.parse(recipeString))).orElse(null);
			}
		}
		return null;
	}
	
	static @Nullable <R extends RecipeInput, T extends Recipe<R>> RecipeHolder<T> getRecipeHolderFromNbt(@Nullable Level world, CompoundTag nbt, Class<T> clazz) {
		var entry = getRecipeHolderFromNbt(world, nbt);
		return entry == null ? null : new RecipeHolder<>(entry.id(), clazz.cast(entry.value()));
	}
	
	static void spawnExperience(Level world, BlockPos blockPos, float amount, RandomSource random) {
		spawnExperience(world, blockPos, Support.getIntFromDecimalWithChance(amount, random));
	}
	
	static void spawnExperience(Level world, BlockPos blockPos, int amount) {
		if (amount > 0) {
			ExperienceOrb experienceOrbEntity = new ExperienceOrb(world, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, amount);
			world.addFreshEntity(experienceOrbEntity);
		}
	}
	
	static void spawnItemStackAsEntitySplitViaMaxCount(Level world, BlockPos blockPos, ItemStack itemStack, int amount, Vec3 velocity) {
		spawnItemStackAsEntitySplitViaMaxCount(world, Vec3.atCenterOf(blockPos), itemStack, amount, velocity, true, null);
	}
	
	static void spawnItemStackAsEntitySplitViaMaxCount(Level world, Vec3 pos, ItemStack itemStack, int amount, Vec3 velocity, boolean neverDespawn, @Nullable Entity owner) {
		while (amount > 0) {
			int currentAmount = Math.min(amount, itemStack.getMaxStackSize());
			
			ItemStack resultStack = itemStack.copy();
			resultStack.setCount(currentAmount);
			ItemEntity itemEntity = new ItemEntity(world, pos.x(), pos.y(), pos.z(), resultStack);
			itemEntity.setDeltaMovement(velocity);
			if (neverDespawn) {
				itemEntity.setUnlimitedLifetime();
			}
			if (owner != null) {
				itemEntity.setTarget(owner.getUUID());
			}
			itemEntity.setExtendedLifetime();
			world.addFreshEntity(itemEntity);
			
			amount -= currentAmount;
		}
	}
	
	static void spawnOutputAsItemEntity(Level world, BlockPos pos, ItemStack outputItemStack) {
		ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, outputItemStack);
		itemEntity.push(0, 0.1, 0);
		itemEntity.setExtendedLifetime();
		world.addFreshEntity(itemEntity);
	}
	
}
