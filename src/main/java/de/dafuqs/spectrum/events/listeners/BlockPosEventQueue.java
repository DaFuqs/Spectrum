package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class BlockPosEventQueue extends EventQueue<BlockPosEventQueue.EventEntry> {
	
	public BlockPosEventQueue(PositionSource positionSource, int range, Callback<EventEntry> listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(Level world, GameEvent.ListenerInfo event, Vec3 sourcePos) {
		if (world instanceof ServerLevel) {
			Vec3 emitterPos = event.source();
			EventEntry eventEntry = new EventEntry(event.gameEvent(), BlockPos.containing(emitterPos.x, emitterPos.y, emitterPos.z), Mth.floor(event.source().distanceTo(sourcePos)));
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			TypedTransmissionPayload.playTransmissionParticle((ServerLevel) world, new TypedTransmission(emitterPos, this.positionSource, delay, TypedTransmission.Variant.BLOCK_POS));
		}
	}
	
	public static class EventEntry {
		public final Holder<GameEvent> gameEvent;
		public final BlockPos eventSourceBlockPos;
		public final int distance;
		
		public EventEntry(Holder<GameEvent> gameEvent, BlockPos eventSourceBlockPos, int distance) {
			this.gameEvent = gameEvent;
			this.eventSourceBlockPos = eventSourceBlockPos;
			this.distance = distance;
		}
	}
	
}