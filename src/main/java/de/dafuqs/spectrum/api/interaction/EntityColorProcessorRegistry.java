package de.dafuqs.spectrum.api.interaction;

import net.minecraft.entity.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class EntityColorProcessorRegistry {
	
	private static final Map<EntityType<?>, BiFunction<Entity, DyeColor, Boolean>> PROCESSOR = new HashMap<>();
	
	public static <E extends Entity> void register(EntityType<E> entityType, EntityColorProcessor<E> processor) {
		BiFunction<Entity, DyeColor, Boolean> ttt = (entity, dyeColor) -> processor.colorEntity((E) entity, dyeColor);
		PROCESSOR.put(entityType, ttt);
	}
	
	public static boolean colorEntity(Entity entity, DyeColor dyeColor) {
		@Nullable BiFunction<Entity, DyeColor, Boolean> colorProcessor = PROCESSOR.getOrDefault(entity.getType(), null);
		if (colorProcessor != null) {
			return colorProcessor.apply(entity, dyeColor);
		}
		return false;
	}
	
}
