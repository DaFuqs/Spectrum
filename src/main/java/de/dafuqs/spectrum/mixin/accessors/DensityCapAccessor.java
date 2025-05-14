package de.dafuqs.spectrum.mixin.accessors;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(LocalMobCapCalculator.MobCounts.class)
public interface DensityCapAccessor {

    @Final
    @Accessor
	Object2IntMap<MobCategory> getCounts();
}
