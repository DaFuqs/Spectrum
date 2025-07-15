package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class ExperienceOrbEventQueue extends EventQueue<ExperienceOrbEventQueue.EventEntry> {
	
	public ExperienceOrbEventQueue(PositionSource positionSource, int range, Callback<EventEntry> listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(Level world, GameEvent.ListenerInfo event, Vec3 sourcePos) {
		if (world instanceof ServerLevel && event.context().sourceEntity() instanceof ExperienceOrb experienceOrbEntity) {
			Vec3 pos = event.source();
			EventEntry eventEntry = new EventEntry(event.gameEvent(), experienceOrbEntity, Mth.floor(pos.distanceTo(sourcePos)));
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			TypedTransmissionPayload.playTransmissionParticle((ServerLevel) world, new TypedTransmission(pos, this.positionSource, delay, TypedTransmission.Variant.EXPERIENCE));
		}
	}
	
	public static class EventEntry {
		public final Holder<GameEvent> event;
		public final ExperienceOrb experienceOrbEntity;
		public final int distance;
		
		public EventEntry(Holder<GameEvent> event, ExperienceOrb experienceOrbEntity, int distance) {
			this.event = event;
			this.experienceOrbEntity = experienceOrbEntity;
			this.distance = distance;
		}
	}
	
}