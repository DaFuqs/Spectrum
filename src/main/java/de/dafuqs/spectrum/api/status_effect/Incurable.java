package de.dafuqs.spectrum.api.status_effect;


import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.SpectrumStatusEffectTags;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.world.*;

public interface Incurable {

    void spectrum$setIncurable(boolean incurable);

    boolean spectrum$isIncurable();

    static boolean isIncurable(StatusEffectInstance instance) {
        var type = instance.getEffectType();
        if (SpectrumStatusEffectTags.cannotBeIncurable(type))
            return false;

        return ((Incurable) instance).spectrum$isIncurable();
    }
	
	static void cutDuration(LivingEntity instance, StatusEffectInstance effect) {
		// new duration = duration - 1min OR duration * 0.4, whichever is the smaller reduction
		int duration = effect.getDuration();
		((StatusEffectInstanceAccessor) effect).setDuration(Math.max(duration - 1200, (int)(duration * 0.4)));
		if (!instance.getWorld().isClient()) {
			((ServerWorld) instance.getWorld()).getChunkManager().sendToNearbyPlayers(instance, new EntityStatusEffectS2CPacket(instance.getId(), effect));
		}
	}
}
