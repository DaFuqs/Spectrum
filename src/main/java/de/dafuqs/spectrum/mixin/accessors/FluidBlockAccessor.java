package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidBlock.class)
public interface FluidBlockAccessor {
	
	@Accessor(value = "fluid")
	FlowableFluid getFlowableFluid();
	
}