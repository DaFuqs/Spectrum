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
public abstract class EntityApplyFluidsMixin {
	
	@Final
	@Shadow
	private Set<TagKey<Fluid>> submergedFluidTag;
	
	@Unique
	private boolean actuallyTouchingWater = false;

	@Unique
	public boolean isActuallyTouchingWater() {
		return this.actuallyTouchingWater;
	}
	
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
	
	// 25.12.2023: Lithium's mixin cancel makes this code not run, making fluids not swimmable
	// https://github.com/CaffeineMC/lithium-fabric/blob/300f430d7b8618ac3b0862892b36696dcfab5a85/src/main/java/me/jellysquid/mods/lithium/mixin/entity/collisions/fluid/EntityMixin.java#L46
	// we therefore disable that mixin in our fabric.mod.json like documented in
	// https://github.com/CaffeineMC/lithium-fabric/wiki/Disabling-Lithium's-Mixins-using-your-mod's-fabric-mod.json
	@Redirect(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	public boolean spectrum$updateMovementInFluid(FluidState fluidState, TagKey<Fluid> tag) {
		if (tag == FluidTags.WATER) {
			return (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.SWIMMABLE_FLUID));
		} else {
			return fluidState.isIn(tag);
		}
	}
	
	@Inject(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void spectrum$updateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> info, Box box, int i, int j, int k, int l, int m, int n, double d, boolean bl, boolean bl2, Vec3d vec3d, int o, BlockPos.Mutable mutable, int p, int q, int r, FluidState fluidState) {
		this.actuallyTouchingWater = fluidState.isIn(FluidTags.WATER);
	}

	@ModifyArg(method = "onSwimmingStart()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), index = 0)
	private ParticleEffect spectrum$modifySwimmingStartParticles(ParticleEffect particleEffect) {
		Fluid fluid = ((Entity) (Object) this).getWorld().getFluidState(((Entity) (Object) this).getBlockPos()).getFluid();
		if (fluid instanceof SpectrumFluid spectrumFluid) {
			return spectrumFluid.getSplashParticle();
		}
		return particleEffect;
	}
	
}
