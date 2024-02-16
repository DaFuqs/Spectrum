package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.recipe.pedestal.*;
import net.minecraft.block.*;

public interface PedestalVariant {
	
	PedestalRecipeTier getRecipeTier();
	
	Block getPedestalBlock();
	
	default boolean isBetterThan(PedestalVariant other) {
		return this.getRecipeTier().ordinal() > other.getRecipeTier().ordinal();
	}
	
	default boolean isEqualOrBetterThan(PedestalVariant other) {
		return this.getRecipeTier().ordinal() >= other.getRecipeTier().ordinal();
	}
	
	
}

