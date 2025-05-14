package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(RangedAttackGoal.class)
public interface ProjectileAttackGoalAccessor {

    @Accessor("target")
    LivingEntity getProjectileAttackTarget();

}