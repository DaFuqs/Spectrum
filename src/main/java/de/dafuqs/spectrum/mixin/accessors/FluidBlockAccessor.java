package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(LiquidBlock.class)
public interface FluidBlockAccessor {
	
	@Accessor("fluid")
	FlowingFluid getFlowableFluid();
	
}