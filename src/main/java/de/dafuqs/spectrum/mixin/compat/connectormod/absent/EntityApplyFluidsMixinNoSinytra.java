package de.dafuqs.spectrum.mixin.compat.connectormod.absent;

import de.dafuqs.spectrum.api.entity.*;
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
import com.llamalad7.mixinextras.injector.wrapoperation.*;

@Mixin(Entity.class)
public class EntityApplyFluidsMixinNoSinytra {

    // 25.12.2023: Lithium's mixin cancel makes this code not run, making fluids not swimmable
    // https://github.com/CaffeineMC/lithium-fabric/blob/300f430d7b8618ac3b0862892b36696dcfab5a85/src/main/java/me/jellysquid/mods/lithium/mixin/entity/collisions/fluid/EntityMixin.java#L46
    // we therefore disable that mixin in our fabric.mod.json like documented in
    // https://github.com/CaffeineMC/lithium-fabric/wiki/Disabling-Lithium's-Mixins-using-your-mod's-fabric-mod.json
    @WrapOperation(method = {"updateMovementInFluid", "updateSwimming"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    public boolean spectrum$updateMovementInFluid(FluidState instance, TagKey<Fluid> tag, Operation<Boolean> original) {
        if (original.call(instance, tag)) return true;
        return tag == FluidTags.WATER ? original.call(instance, SpectrumFluidTags.SWIMMABLE_FLUID) : false;
    }

    // Used to cache the state being submerged in water, which is used for initiating swimming.
    // Expanded by including Spectrum's swimmable fluids in the check.
    @WrapOperation(method = "updateSubmergedInWaterState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    public boolean spectrum$updateSubmergedInWaterState(Entity instance, TagKey<Fluid> tag, Operation<Boolean> original) {
        if (original.call(instance, tag)) return true;
        return tag == FluidTags.WATER ? original.call(instance, SpectrumFluidTags.SWIMMABLE_FLUID) : false;
    }

    @ModifyArg(method = "onSwimmingStart()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), index = 0)
    private ParticleEffect spectrum$modifySwimmingStartParticles(ParticleEffect particleEffect) {
        Fluid fluid = ((Entity) (Object) this).getWorld().getFluidState(((Entity) (Object) this).getBlockPos()).getFluid();
        if (fluid instanceof SpectrumFluid spectrumFluid) {
            return spectrumFluid.getSplashParticle();
        }
        return particleEffect;
    }

    @Inject(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void spectrum$updateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> info, Box box, int i, int j, int k, int l, int m, int n, double d, boolean bl, boolean bl2, Vec3d vec3d, int o, BlockPos.Mutable mutable, int p, int q, int r, FluidState fluidState) {
        ((TouchingWaterAware) this).spectrum$setActuallyTouchingWater(fluidState.isIn(FluidTags.WATER));
    }
}
