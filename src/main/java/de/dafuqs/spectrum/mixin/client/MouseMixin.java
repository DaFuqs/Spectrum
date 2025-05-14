package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MouseHandler.class)
public class MouseMixin {
	
	@SuppressWarnings("unchecked")
	@ModifyExpressionValue(method = "turnPlayer", at = @At(value = "INVOKE", target = "net/minecraft/client/OptionInstance.get ()Ljava/lang/Object;", ordinal = 0))
	public <T> T spectrum$makeMouseSluggish(T original) {
		var sensitivity = (double) original;
		var player = Minecraft.getInstance().player;
		
		if (player == null)
			return original;
		
		var potency = SleepStatusEffect.getSleepScaling(player);
		
		if (potency == -1)
			return original;
		
		return (T) (Object) Mth.clampedLerp(sensitivity, sensitivity / 2, potency / 2.5);
	}
	
	// AHAHAHAH, I FINALLY FIGURED OUT HOW TO DO IT!
	@ModifyExpressionValue(method = "turnPlayer", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/Options;smoothCamera:Z"))
	public boolean spectrum$forceSmoothCamera(boolean original) {
		var player = Minecraft.getInstance().player;
		
		if (player == null)
			return original;
		
		var potency = SleepStatusEffect.getSleepScaling(player);
		
		if (potency < 1.9)
			return original;
		
		return true;
	}
}
