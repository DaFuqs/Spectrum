package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.entity.projectile.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(AbstractArrow.class)
public interface PersistentProjectileEntityAccessor {
	
	@Accessor
	int getLife();
	
	@Accessor
	void setLife(int life);
	
}
