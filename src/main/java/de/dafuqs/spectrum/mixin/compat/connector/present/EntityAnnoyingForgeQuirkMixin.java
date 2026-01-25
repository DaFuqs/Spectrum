package de.dafuqs.spectrum.mixin.compat.connector.present;

import com.llamalad7.mixinextras.sugar.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.material.*;
import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Entity.class)
public class EntityAnnoyingForgeQuirkMixin {
	
	@Shadow
	@Final
	private Set<TagKey<Fluid>> fluidOnEyes;
	
	@Inject(method = "updateFluidOnEyes", at = @At(value = "FIELD", target = "forgeFluidTypeOnEyes", opcode = Opcodes.PUTFIELD, remap = false))
	public void spectrum$actuallyUpdateSubmergedInWaterStateOnStupidForge(CallbackInfo ci, @Local FluidState fluidState) {
		fluidState.getTags().forEach(this.fluidOnEyes::add);
	}
	
}
