package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(Item.class)
public interface ItemAccessor {
	
	@Accessor(value = "ATTACK_SPEED_MODIFIER_ID")
	static UUID getAttackSpeedModifierId() {
		throw new AssertionError();
	}
	
}