package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Entity.class)
public abstract class EntityApplyFluidsMixin {
	
	@Final
	@Shadow
	private Set<TagKey<Fluid>> submergedFluidTag;
	
	@Shadow
	public abstract void readNbt(NbtCompound nbt);
	
	@Inject(method = "isSubmergedIn", at = @At("RETURN"), cancellable = true)
	public void spectrum$isSubmergedIn(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && fluidTag == FluidTags.WATER) {
			cir.setReturnValue(this.submergedFluidTag.contains(fluidTag) || this.submergedFluidTag.contains(SpectrumFluidTags.SWIMMABLE_FLUID));
		}
	}
	
	@Redirect(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	public boolean spectrum$updateMovementInFluid(FluidState fluidState, TagKey<Fluid> tag) {
		return isInFluid(fluidState, tag);
	}
	
	private boolean isInFluid(FluidState fluidState, TagKey<Fluid> tag) {
		if (tag == FluidTags.WATER) {
			return (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.SWIMMABLE_FLUID));
		} else {
			return fluidState.isIn(tag);
		}
	}
	
	@ModifyArg(method = "onSwimmingStart()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), index = 0)
	private ParticleEffect spectrum$modifySwimmingStartParticles(ParticleEffect particleEffect) {
		Fluid fluid = ((Entity) (Object) this).world.getFluidState(((Entity) (Object) this).getBlockPos()).getFluid();
		if (fluid instanceof SpectrumFluid spectrumFluid) {
			return spectrumFluid.getSplashParticle();
		}
		return particleEffect;
	}
	
}
