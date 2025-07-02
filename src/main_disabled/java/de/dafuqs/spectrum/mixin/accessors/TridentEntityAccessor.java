package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.projectile.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ThrownTrident.class)
public interface TridentEntityAccessor {
	
	@Accessor("ID_LOYALTY")
	static EntityDataAccessor<Byte> spectrum$getLoyalty() {
		return null;
	}
	
	@Accessor("ID_FOIL")
	static EntityDataAccessor<Boolean> spectrum$getEnchanted() {
		return null;
	}
	
	@Accessor("dealtDamage")
	void spectrum$setDealtDamage(boolean dealtDamage);
	
}