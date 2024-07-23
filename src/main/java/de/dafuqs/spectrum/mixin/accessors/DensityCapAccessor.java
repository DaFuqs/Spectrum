package de.dafuqs.spectrum.mixin.accessors;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnDensityCapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnDensityCapper.DensityCap.class)
public interface DensityCapAccessor {

    @Final
    @Accessor
    Object2IntMap<SpawnGroup> getSpawnGroupsToDensity();
}
