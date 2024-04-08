package de.dafuqs.spectrum.api.interaction;

import net.minecraft.entity.*;
import net.minecraft.util.*;

public interface EntityColorProcessor<E extends Entity> {
	
	/**
	 * Logic for coloring an entity type in a dyecolor
	 *
	 * @param entity   The entity to be colored
	 * @param dyeColor The dyeColor to color the entity in
	 * @return if the coloring was successful (true if colored, false when failed, like the entity already being that color)
	 */
	boolean colorEntity(E entity, DyeColor dyeColor);
	
}
