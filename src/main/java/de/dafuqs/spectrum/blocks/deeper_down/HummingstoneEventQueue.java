package de.dafuqs.spectrum.blocks.deeper_down;

import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.events.listeners.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class HummingstoneEventQueue extends EventQueue<HummingstoneEventQueue.EventEntry> {
    
    public HummingstoneEventQueue(PositionSource positionSource, int range, Callback<EventEntry> listener) {
        super(positionSource, range, listener);
    }
    
    @Override
	public void acceptEvent(Level world, GameEvent.ListenerInfo message, Vec3 sourcePos) {
		Vec3 pos = message.source();
		EventEntry eventEntry = new EventEntry(message, Mth.floor(pos.distanceTo(sourcePos)));
        int delay = eventEntry.distance * 2;
		this.schedule(eventEntry, delay);
		
		if (message.gameEvent() == SpectrumGameEvents.HUMMINGSTONE_HUMMING) {
			TypedTransmissionPayload.playTransmissionParticle((ServerLevel) world, new TypedTransmission(pos, this.positionSource, delay, TypedTransmission.Variant.HUMMINGSTONE));
			if (getQueuedEventCount() > 20) {
				world.gameEvent(message.context().sourceEntity(), SpectrumGameEvents.HUMMINGSTONE_HYMN, pos);
			}
		}
	}
	
	public record EventEntry(GameEvent.ListenerInfo message, int distance) {
	}

}
