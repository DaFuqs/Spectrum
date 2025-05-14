package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.recipe.pedestal.*;
import net.minecraft.world.level.block.*;

public interface PedestalVariant {
	
	PedestalRecipeTier getRecipeTier();
	
	Block getPedestalBlock();
	
	default boolean isBetterThan(PedestalVariant other) {
		return this.getRecipeTier().ordinal() > other.getRecipeTier().ordinal();
	}
	
}