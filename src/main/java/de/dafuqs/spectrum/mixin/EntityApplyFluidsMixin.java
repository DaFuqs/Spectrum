package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Entity.class)
public abstract class EntityApplyFluidsMixin {
	
	@Final
	@Shadow
	private Set<TagKey<Fluid>> submergedFluidTag;
	
	private boolean actuallyTouchingWater = false;
	
	@Shadow
	public abstract void readNbt(NbtCompound nbt);
	
	public boolean isActuallyTouchingWater() {
		return this.actuallyTouchingWater;
	}
	
	@Inject(method = "isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z", at = @At("RETURN"), cancellable = true)
	public void spectrum$isSubmergedIn(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && fluidTag == FluidTags.WATER) {
			cir.setReturnValue(this.submergedFluidTag.contains(fluidTag) || this.submergedFluidTag.contains(SpectrumFluidTags.SWIMMABLE_FLUID));
		}
	}
	
	@Redirect(method = "updateMovementInFluid(Lnet/minecraft/tag/TagKey;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"))
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
	
	@Inject(method = "updateMovementInFluid(Lnet/minecraft/tag/TagKey;D)Z", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void spectrum$updateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> info, Box box, int i, int j, int k, int l, int m, int n, double d, boolean bl, boolean bl2, Vec3d vec3d, int o, BlockPos.Mutable mutable, int p, int q, int r, FluidState fluidState) {
		this.actuallyTouchingWater = fluidState.isIn(FluidTags.WATER);
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
