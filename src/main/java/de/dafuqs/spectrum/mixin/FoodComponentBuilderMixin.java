package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.injectors.*;
import net.minecraft.world.food.*;
import org.spongepowered.asm.mixin.*;

@Mixin(FoodProperties.Builder.class)
public abstract class FoodComponentBuilderMixin implements FoodComponentBuilderInjector {
	
	@Shadow
	private float eatSeconds;
	
	@Override
	public FoodProperties.Builder spectrum$setEatSeconds(float eatSeconds) {
		this.eatSeconds = eatSeconds;
		return (FoodProperties.Builder) (Object) this;
	}
	
}
