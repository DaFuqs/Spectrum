package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Mob.class)
public interface MobEntityAccessor {
	
	@Invoker
	float invokeGetEquipmentDropChance(EquipmentSlot slot);
	
}