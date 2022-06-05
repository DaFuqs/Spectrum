package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Entity.class)
public class EntityApplyFluidsMixin {
	
	@Final
	@Shadow
	private Set<TagKey<Fluid>> submergedFluidTag;
	
	@Inject(at = @At("RETURN"), method = "isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z", cancellable = true)
	public void isSubmergedIn(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && fluidTag == FluidTags.WATER) {
			cir.setReturnValue(this.submergedFluidTag.contains(fluidTag) || this.submergedFluidTag.contains(SpectrumFluidTags.SWIMMABLE_FLUID));
		}
	}
	
	@Redirect(method = "updateMovementInFluid(Lnet/minecraft/tag/TagKey;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"))
	public boolean updateMovementInFluid(FluidState fluidState, TagKey<Fluid> tag) {
		return isInFluid(fluidState, tag);
	}
	
	private boolean isInFluid(FluidState fluidState, TagKey<Fluid> tag) {
		if (tag == FluidTags.WATER) {
			return (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.SWIMMABLE_FLUID));
		} else {
			return fluidState.isIn(tag);
		}
	}
	
}
