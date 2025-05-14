package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class ItemEntityEventQueue extends EventQueue<ItemEntityEventQueue.EventEntry> {
	
	public ItemEntityEventQueue(PositionSource positionSource, int range, Callback<EventEntry> listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(Level world, GameEvent.ListenerInfo event, Vec3 sourcePos) {
		if (world instanceof ServerLevel && event.context().sourceEntity() instanceof ItemEntity itemEntity) {
			Vec3 pos = event.source();
			EventEntry eventEntry = new EventEntry(event.gameEvent(), itemEntity, Mth.floor(pos.distanceTo(sourcePos)));
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			TypedTransmissionPayload.playTransmissionParticle((ServerLevel) world, new TypedTransmission(pos, this.positionSource, delay, TypedTransmission.Variant.ITEM));
		}
	}
	
	public static class EventEntry {
		public final Holder<GameEvent> event;
		public final ItemEntity itemEntity;
		public final int distance;
		
		public EventEntry(Holder<GameEvent> event, ItemEntity itemEntity, int distance) {
			this.event = event;
			this.itemEntity = itemEntity;
			this.distance = distance;
		}
	}
	
}