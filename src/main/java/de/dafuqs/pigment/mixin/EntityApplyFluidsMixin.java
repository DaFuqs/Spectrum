package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.registries.PigmentFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityApplyFluidsMixin {

    @Redirect(method = "updateSubmergedInWaterState()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSubmergedIn(Lnet/minecraft/tag/Tag;)Z"))
    private boolean overwriteWaterStates(Entity entity, Tag<Fluid> fluidTag) {
        return entity.isSubmergedIn(FluidTags.WATER) || entity.isSubmergedIn(PigmentFluidTags.LIQUID_CRYSTAL) || entity.isSubmergedIn(PigmentFluidTags.MUD);
    }

}
