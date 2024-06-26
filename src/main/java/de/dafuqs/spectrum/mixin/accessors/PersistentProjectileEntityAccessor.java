package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.projectile.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileEntityAccessor {
	
	@Accessor
	int getLife();
	
	@Accessor
	void setLife(int life);
}
