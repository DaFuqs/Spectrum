package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(MobEntity.class)
public interface MobEntityAccessor {
	
	@Invoker("getDropChance")
	float invokeGetDropChance(EquipmentSlot slot);
	
}