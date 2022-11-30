package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.fluid.SpectrumFluid;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityApplyFluidsMixin {
	
	@Final
	@Shadow
	private Set<TagKey<Fluid>> submergedFluidTag;
	
	@Shadow
	public abstract void readNbt(NbtCompound nbt);
	
	@Inject(method = "isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z", at = @At("RETURN"), cancellable = true)
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
	
	@ModifyArg(method = "onSwimmingStart()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), index = 0)
	private ParticleEffect spectrum$modifySwimmingStartParticles(ParticleEffect particleEffect) {
		Fluid fluid = ((Entity) (Object) this).world.getFluidState(((Entity) (Object) this).getBlockPos()).getFluid();
		if (fluid instanceof SpectrumFluid spectrumFluid) {
			return spectrumFluid.getSplashParticle();
		}
		return particleEffect;
	}
	
}
