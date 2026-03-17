package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Entity.class)
public class EntityApplyFluidsMixinNoSinytra {

    // 25.12.2023: Lithium's mixin cancel makes this code not run, making fluids not swimmable
    // https://github.com/CaffeineMC/lithium-fabric/blob/300f430d7b8618ac3b0862892b36696dcfab5a85/src/main/java/me/jellysquid/mods/lithium/mixin/entity/collisions/fluid/EntityMixin.java#L46
    // we therefore disable that mixin in our fabric.mod.json like documented in
    // https://github.com/CaffeineMC/lithium-fabric/wiki/Disabling-Lithium's-Mixins-using-your-mod's-fabric-mod.json
	@WrapOperation(method = {"updateFluidHeightAndDoFluidPushing", "updateSwimming"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean spectrum$updateMovementInFluid(FluidState instance, TagKey<Fluid> tag, Operation<Boolean> original) {
        if (original.call(instance, tag)) return true;
        return tag == FluidTags.WATER ? original.call(instance, SpectrumFluidTags.SWIMMABLE_FLUID) : false;
    }

    // Used to cache the state being submerged in water, which is used for initiating swimming.
    // Expanded by including Spectrum's swimmable fluids in the check.
	@WrapOperation(method = "updateFluidOnEyes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean spectrum$updateSubmergedInWaterState(Entity instance, TagKey<Fluid> tag, Operation<Boolean> original) {
        if (original.call(instance, tag)) return true;
        return tag == FluidTags.WATER ? original.call(instance, SpectrumFluidTags.SWIMMABLE_FLUID) : false;
    }
	
	@ModifyArg(method = "doWaterSplashEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"), index = 0)
	private ParticleOptions spectrum$modifySwimmingStartParticles(ParticleOptions particleEffect) {
		Fluid fluid = ((Entity) (Object) this).level().getFluidState(((Entity) (Object) this).blockPosition()).getType();
        if (fluid instanceof SpectrumFluid spectrumFluid) {
            return spectrumFluid.getSplashParticle();
        }
        return particleEffect;
    }
	
	@Inject(method = "updateFluidHeightAndDoFluidPushing", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void spectrum$updateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> info, AABB box, int i, int j, int k, int l, int m, int n, double d, boolean bl, boolean bl2, Vec3 vec3d, int o, BlockPos.MutableBlockPos mutable, int p, int q, int r, FluidState fluidState) {
		((TouchingWaterAware) this).spectrum$setActuallyTouchingWater(fluidState.is(FluidTags.WATER));
    }
}
