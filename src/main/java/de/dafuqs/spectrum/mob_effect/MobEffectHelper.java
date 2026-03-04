package de.dafuqs.spectrum.mob_effect;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;

public class MobEffectHelper {
	
	private static final float ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL = 0.25F;
	
	public static boolean canBeExtended(Holder<MobEffect> statusEffect) {
		return !statusEffect.is(SpectrumMobEffectTags.NO_DURATION_EXTENSION);
	}
	
	public static int getExtendedDuration(int originalDuration, int prolongingAmplifier) {
		return (int) (originalDuration * (1 + ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL * (1 + prolongingAmplifier)));
	}
	
	public static boolean isStrongSleepEffect(MobEffectInstance instance) {
		return instance.getEffect() == SpectrumMobEffects.ETERNAL_SLUMBER || instance.getEffect() == SpectrumMobEffects.FATAL_SLUMBER;
	}
	
	public static boolean isStrongSleepEffect(InkPoweredStatusEffectInstance instance) {
		return isStrongSleepEffect(instance.getStatusEffectInstance());
	}
	
	public static void cutDuration(LivingEntity instance, MobEffectInstance effect) {
		// new duration = duration - 1min OR duration * 0.4, whichever is the smaller reduction
		int duration = effect.getDuration();
		((StatusEffectInstanceAccessor) effect).setDuration(Math.max(duration - 1200, (int) (duration * 0.4)));
		if (instance.level() instanceof ServerLevel serverWorld) {
			serverWorld.getChunkSource().broadcastAndSend(instance, new ClientboundUpdateMobEffectPacket(instance.getId(), effect, false));
		}
	}
}
