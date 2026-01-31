package de.dafuqs.spectrum.injectors;

import net.minecraft.server.level.*;
import org.apache.commons.lang3.*;

import java.util.function.*;

public interface SimpleCriterionTriggerInjector {
	
	default void spectrum$trigger(ServerPlayer player, Predicate testTrigger) {
		throw new NotImplementedException();
	}
	
}
