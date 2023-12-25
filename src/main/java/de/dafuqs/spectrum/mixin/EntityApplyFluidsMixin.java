package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Entity.class)
public abstract class EntityApplyFluidsMixin implements TouchingWaterAware{
	
	@Final
	@Shadow
	private Set<TagKey<Fluid>> submergedFluidTag;

	@Unique
	private boolean actuallyTouchingWater = false;

	@Unique
	public boolean spectrum$isActuallyTouchingWater() {
		return this.actuallyTouchingWater;
	}

	@Unique
	public void spectrum$setActuallyTouchingWater(boolean actuallyTouchingWater) { this.actuallyTouchingWater = actuallyTouchingWater; }
	
	@Inject(method = "isSubmergedIn", at = @At("RETURN"), cancellable = true)
	public void spectrum$isSubmergedIn(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && fluidTag == FluidTags.WATER) {
			cir.setReturnValue(this.submergedFluidTag.contains(SpectrumFluidTags.SWIMMABLE_FLUID));
		}
	}
	
	@Inject(method = "isSubmergedInWater", at = @At("RETURN"), cancellable = true)
	public void spectrum$isSubmergedInWater(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && this.submergedFluidTag.contains(SpectrumFluidTags.SWIMMABLE_FLUID)) {
			//this.submergedFluidTag.add(FluidTags.WATER);
			cir.setReturnValue(true);
		}
	}
	

	



	
}
