package de.dafuqs.spectrum.injectors;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.food.FoodProperties;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.*;

public interface SimpleCriterionTriggerInjector {
	
	default void spectrum$trigger(ServerPlayer player, Predicate testTrigger) {
		throw new NotImplementedException();
	}
	
}
