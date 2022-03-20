package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.Tag;
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

	/*
	TODO
	@Inject(at = @At("HEAD"), method = "isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z", cancellable = true)
	public void isSubmergedIn(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
		if(fluidTag == FluidTags.WATER) {
			cir.setReturnValue(this.submergedFluidTag == fluidTag
					|| this.submergedFluidTag == SpectrumFluidTags.MUD
					|| this.submergedFluidTag == SpectrumFluidTags.LIQUID_CRYSTAL
					|| this.submergedFluidTag == SpectrumFluidTags.MIDNIGHT_SOLUTION
			);
		}
	}

	@Redirect(method = "updateMovementInFluid(Lnet/minecraft/tag/TagKey;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/Tag;)Z"))
	public boolean updateMovementInFluid(FluidState fluidState, Tag<Fluid> tag) {
		  return isInFluid(fluidState, tag);
	}

	private boolean isInFluid(FluidState fluidState, Tag<Fluid> tag) {
		if(tag == FluidTags.WATER) {
			return(fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.MUD) || fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL) || fluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION));
		} else {
			return fluidState.isIn(tag);
		}
	}*/


}
