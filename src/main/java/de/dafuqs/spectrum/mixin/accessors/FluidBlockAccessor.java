package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.block.*;
import net.minecraft.fluid.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(FluidBlock.class)
public interface FluidBlockAccessor {
	
	@Accessor(value = "fluid")
	FlowableFluid getFlowableFluid();
	
}