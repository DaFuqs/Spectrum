package de.dafuqs.spectrum.mixin.accessors;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.render.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(DimensionEffects.class)
public interface DimensionEffectsAccessor {

    @Accessor(value = "BY_IDENTIFIER")
    static Object2ObjectMap<Identifier, DimensionEffects> getEffects() {
        throw new AssertionError();
    }

}