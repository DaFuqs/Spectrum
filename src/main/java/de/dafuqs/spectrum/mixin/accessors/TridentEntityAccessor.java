package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.projectile.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ThrownTrident.class)
public interface TridentEntityAccessor {
	
	@Accessor("dealtDamage")
	void spectrum$setDealtDamage(boolean dealtDamage);
	
}