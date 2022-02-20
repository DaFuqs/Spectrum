package de.dafuqs.spectrum.recipe;

import net.minecraft.entity.player.PlayerEntity;

public interface GatedRecipe {
	
	boolean canPlayerCraft(PlayerEntity playerEntity);
	
}
