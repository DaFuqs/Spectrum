package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.api.ink.color.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.apache.commons.lang3.function.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

public class EntityColorProcessorRegistry {
	
	private static final Map<Supplier<EntityType<?>>, TriFunction<Entity, Optional<InkColor>, Player, Boolean>> PROCESSOR = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <E extends Entity> void register(Supplier<EntityType<E>> entityType, EntityColorProcessor<E> processor) {
		TriFunction<Entity, Optional<InkColor>, Player, Boolean> ttt = (entity, color, player) -> processor.colorEntity((E) entity, color, player);
		PROCESSOR.put(entityType::get, ttt);
	}
	
	public static boolean colorEntity(Entity entity, Optional<InkColor> color, @Nullable Player player) {
		for(Map.Entry<Supplier<EntityType<?>>, TriFunction<Entity, Optional<InkColor>, Player, Boolean>> entry : PROCESSOR.entrySet()) {
			if(entry.getKey().get() == entity.getType()) {
				return entry.getValue().apply(entity, color, player);
			}
		}
		return false;
	}
	
}
