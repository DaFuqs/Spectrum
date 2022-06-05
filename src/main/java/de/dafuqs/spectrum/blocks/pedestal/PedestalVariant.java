package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import net.minecraft.block.Block;

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

