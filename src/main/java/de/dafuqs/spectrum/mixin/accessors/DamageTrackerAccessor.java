package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.damage.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(DamageTracker.class)
public interface DamageTrackerAccessor {
	
	@Accessor(value = "recentDamage")
	List<DamageRecord> getRecentDamage();
	
}