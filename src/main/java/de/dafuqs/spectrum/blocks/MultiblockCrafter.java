package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiblockCrafter extends RecipeInputProvider, Upgradeable, PlayerOwned {
	
	static void spawnExperience(World world, BlockPos blockPos, int spawnedXPAmount) {
		if (spawnedXPAmount > 0) {
			ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, spawnedXPAmount);
			world.spawnEntity(experienceOrbEntity);
		}
	}
	
}
