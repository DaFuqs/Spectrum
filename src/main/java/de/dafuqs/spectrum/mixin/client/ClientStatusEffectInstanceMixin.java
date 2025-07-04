package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.injectors.*;
import net.fabricmc.api.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Environment(EnvType.CLIENT)
@Mixin(MobEffectInstance.class)
public abstract class ClientStatusEffectInstanceMixin implements StatusEffectInstanceInjector {
	
	@ModifyReturnValue(method = "describeDuration()Ljava/lang/String;", at = @At("RETURN"))
	private String describeDuration(String original) {
		if (this.spectrum$isSevere()) {
			original = original + I18n.get("item.spectrum.potion.tooltip.severe");
		}
		return original;
	}
	
}
