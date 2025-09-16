package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Gui.HeartType.class)
public abstract class ModifyHeartsMixin {
	
	@ModifyVariable(method = "getSprite(ZZZ)Lnet/minecraft/resources/ResourceLocation;", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
	private static boolean spectrum$hardcoreHearts(boolean hardcore) {
		Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
		if (cameraEntity instanceof LivingEntity livingEntity && livingEntity.hasEffect(SpectrumStatusEffects.DIVINITY)) {
			return true;
		}
		return hardcore;
	}
	
	@Inject(method = "forPlayer(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/client/gui/Gui$HeartType;", at = @At(value = "HEAD"), cancellable = true)
	private static void spectrum$deadlyPoisonHearts(Player player, CallbackInfoReturnable<Gui.HeartType> cir) {
		if (player.hasEffect(SpectrumStatusEffects.DEADLY_POISON)) {
			cir.setReturnValue(Gui.HeartType.POISIONED);
		}
	}
	
}
