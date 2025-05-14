package de.dafuqs.spectrum.injectors;

import net.minecraft.world.food.*;
import org.apache.commons.lang3.*;

public interface FoodComponentBuilderInjector {
	
	default FoodProperties.Builder spectrum$setEatSeconds(float eatSeconds) {
		throw new NotImplementedException();
	}
	
}
