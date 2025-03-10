package de.dafuqs.spectrum.mixin.compat.quilt_status_effect.present;

import de.dafuqs.spectrum.api.status_effect.Incurable;
import de.dafuqs.spectrum.mixin.accessors.StatusEffectInstanceAccessor;
import de.dafuqs.spectrum.registries.SpectrumEventListeners;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(value = SpectrumEventListeners.class, remap = false)
public class SpectrumEventListenersMixin {
	// used by ASM; See QSLCompatASM. Do not touch without first consulting QSLCompatASM.
	// MUST: have 3 arguments, arguments must be of (LivingEntity entity, StatusEffectInstance effect, Object reason),
	// return FAPI TriState -- MUST NOT USE TriState anywhere EXCEPT as return value.
	@Unique
	private static TriState _shouldRemove(LivingEntity entity, StatusEffectInstance effect, Object reason) {
		if (Incurable.isIncurable(effect) && !affectedByImmunity(entity, effect.getAmplifier())) {
			// new duration = duration - 1min OR duration * 0.4, whichever is the smaller reduction
			int duration = effect.getDuration();
			((StatusEffectInstanceAccessor) effect).setDuration(Math.max(duration - 1200, (int)(duration * 0.4)));
			if (!entity.getWorld().isClient()) {
				((ServerWorld) entity.getWorld()).getChunkManager().sendToNearbyPlayers(entity, new EntityStatusEffectS2CPacket(entity.getId(), effect));
			}
			return TriState.FALSE;
		}
		return TriState.DEFAULT;
	}

	@Unique
	private static boolean affectedByImmunity(LivingEntity instance, int amplifier) {
		var immunity = instance.getStatusEffect(SpectrumStatusEffects.IMMUNITY);
		var cost = 1200 + 600 * amplifier;

		if (immunity != null && immunity.getDuration() >= cost) {
			((StatusEffectInstanceAccessor) immunity).setDuration(Math.max(5, immunity.getDuration() - cost));
			if (!instance.getWorld().isClient()) {
				((ServerWorld) instance.getWorld()).getChunkManager().sendToNearbyPlayers(instance, new EntityStatusEffectS2CPacket(instance.getId(), immunity));
			}
			return true;
		}
		return false;
	}
}
