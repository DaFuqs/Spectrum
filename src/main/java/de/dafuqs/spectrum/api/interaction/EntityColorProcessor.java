package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.api.ink.color.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.*;

import java.util.*;

public interface EntityColorProcessor<E extends Entity> {
	
	/**
	 * Logic for coloring an entity type in a dye color
	 *
	 * @param entity The entity to be colored
	 * @param color The color to color the entity in. Empty to remove coloring
	 * @param player The player that dyed the entity
	 * @return if the coloring was successful (true if colored, false if failed, like the entity already being that color, or the color not being applicable)
	 */
	boolean colorEntity(E entity, Optional<InkColor> color, @Nullable Player player);
	
}
