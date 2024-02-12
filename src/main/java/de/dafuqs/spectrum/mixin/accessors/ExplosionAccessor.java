package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.explosion.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Explosion.class)
public interface ExplosionAccessor {

    @Accessor(value = "power")
    float getPower();

}