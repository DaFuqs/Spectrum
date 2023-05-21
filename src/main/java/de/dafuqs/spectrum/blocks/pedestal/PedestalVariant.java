package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.enums.*;
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

