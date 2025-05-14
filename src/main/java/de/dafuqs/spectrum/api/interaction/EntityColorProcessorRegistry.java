package de.dafuqs.spectrum.api.interaction;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.apache.commons.lang3.function.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityColorProcessorRegistry {
	
	private static final Map<EntityType<?>, TriFunction<Entity, Optional<DyeColor>, Player, Boolean>> PROCESSOR = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <E extends Entity> void register(EntityType<E> entityType, EntityColorProcessor<E> processor) {
		TriFunction<Entity, Optional<DyeColor>, Player, Boolean> ttt = (entity, dyeColor, player) -> processor.colorEntity((E) entity, dyeColor, player);
		PROCESSOR.put(entityType, ttt);
	}
	
	public static boolean colorEntity(Entity entity, Optional<DyeColor> dyeColor, @Nullable Player player) {
		@Nullable TriFunction<Entity, Optional<DyeColor>, Player, Boolean> colorProcessor = PROCESSOR.getOrDefault(entity.getType(), null);
		if (colorProcessor != null) {
			return colorProcessor.apply(entity, dyeColor, player);
		}
		return false;
	}
	
}
