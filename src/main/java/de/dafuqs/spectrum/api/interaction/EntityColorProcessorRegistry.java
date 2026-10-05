package de.dafuqs.spectrum.api.interaction;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.apache.commons.lang3.function.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

public class EntityColorProcessorRegistry {
	
	private static final Map<Supplier<EntityType<?>>, TriFunction<Entity, Optional<DyeColor>, Player, Boolean>> PROCESSOR = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <E extends Entity> void register(Supplier<EntityType<E>> entityType, EntityColorProcessor<E> processor) {
		TriFunction<Entity, Optional<DyeColor>, Player, Boolean> ttt = (entity, dyeColor, player) -> processor.colorEntity((E) entity, dyeColor, player);
		PROCESSOR.put(entityType::get, ttt);
	}
	
	// todo: refactor dyecolor to inkcolor?
	public static boolean colorEntity(Entity entity, Optional<DyeColor> dyeColor, @Nullable Player player) {
		for(Map.Entry<Supplier<EntityType<?>>, TriFunction<Entity, Optional<DyeColor>, Player, Boolean>> entry : PROCESSOR.entrySet()) {
			if(entry.getKey().get() == entity.getType()) {
				return entry.getValue().apply(entity, dyeColor, player);
			}
		}
		return false;
	}
	
}
