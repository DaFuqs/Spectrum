package de.dafuqs.spectrum.api.interaction;

import net.minecraft.entity.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface EntityColorProcessor<E extends Entity> {
	
	Map<EntityType<?>, EntityColorProcessor<?>> PROCESSOR = new HashMap<>();
	
	static void register(EntityType<?> entityType, EntityColorProcessor<?> processor) {
		PROCESSOR.put(entityType, processor);
	}
	
	static @Nullable EntityColorProcessor<?> get(EntityType<?> entityType) {
		return PROCESSOR.getOrDefault(entityType, null);
	}
	
	/**
	 * Logic for coloring an entity type in a dyecolor
	 *
	 * @param entity   The entity to be colored
	 * @param dyeColor The dyeColor to color the entity in
	 * @return if the coloring was successful (true if colored, false when failed, like the entity already being that color)
	 */
	boolean colorEntity(E entity, DyeColor dyeColor);
	
}
