package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Gui.HeartType.class)
public abstract class ModifyHeartsMixin {
	
	@Inject(method = "Lnet/minecraft/client/gui/Gui$HeartType;forPlayer(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/client/gui/Gui$HeartType;", at = @At(value = "HEAD"), cancellable = true)
	private void spectrum$showDivinityHardcoreHearts(Player player, CallbackInfoReturnable<Gui.HeartType> cir) {
		if (player.hasEffect(SpectrumStatusEffects.DIVINITY)) {
			// cir.setReturnValue(Gui.HeartType.);
		}
		if (player.hasEffect(SpectrumStatusEffects.DEADLY_POISON)) {
			// cir.setReturnValue(Gui.HeartType.);
		}
	}
	
}
