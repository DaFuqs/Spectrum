package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(Item.class)
public interface ItemAccessor {
	
	@Accessor(value = "ATTACK_SPEED_MODIFIER_ID")
	static UUID getAttackSpeedModifierId() {
		throw new AssertionError();
	}
	
}