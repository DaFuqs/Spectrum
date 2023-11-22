package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ProjectileAttackGoal.class)
public interface ProjectileAttackGoalAccessor {

    @Accessor("target")
    LivingEntity getProjectileAttackTarget();

}