package de.dafuqs.spectrum.mixin.compat.connectormod.present;

import com.llamalad7.mixinextras.sugar.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.registry.tag.*;
import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Entity.class)
public class EntityAnnoyingForgeQuirkMixin {
	@Shadow
	@Final
	private Set<TagKey<Fluid>> submergedFluidTag;
	
	@Inject(method = "updateSubmergedInWaterState", at = @At(value = "FIELD", target = "forgeFluidTypeOnEyes", opcode = Opcodes.PUTFIELD, remap = false))
	public void spectrum$actuallyUpdateSubmergedInWaterStateOnStupidForge(CallbackInfo ci, @Local FluidState fluidState) {
		fluidState.streamTags().forEach(this.submergedFluidTag::add);
	}
}
