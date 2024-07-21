package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin extends TrackTargetGoal {

    @Shadow @Nullable protected LivingEntity targetEntity;

    public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
        throw new IllegalStateException();
    }

    @Inject(method = "findClosestTarget", at = @At("TAIL"))
    protected void adjustTargetForCalming(CallbackInfo ci) {
        if (targetEntity == null)
            return;

        var calming = targetEntity.getStatusEffect(SpectrumStatusEffects.CALMING);

        if (calming == null)
            return;

        if (mob.distanceTo(targetEntity) > getFollowRange() * SpectrumStatusEffects.getCalmingMultiplier(calming))
            targetEntity = null;
    }
}
