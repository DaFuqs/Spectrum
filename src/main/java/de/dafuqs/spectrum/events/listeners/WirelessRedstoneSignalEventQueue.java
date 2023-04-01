package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class WirelessRedstoneSignalEventQueue extends EventQueue<WirelessRedstoneSignalEventQueue.EventEntry> {
	
	public WirelessRedstoneSignalEventQueue(PositionSource positionSource, int range, EventQueue.Callback listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(World world, GameEvent.Message event, Vec3d sourcePos) {
		if (world instanceof ServerWorld && event.getEvent() instanceof RedstoneTransferGameEvent redstoneTransferEvent) {
			Vec3d pos = event.getEmitterPos();
			WirelessRedstoneSignalEventQueue.EventEntry eventEntry = new WirelessRedstoneSignalEventQueue.EventEntry(redstoneTransferEvent, MathHelper.floor(pos.distanceTo(sourcePos)));
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			SpectrumS2CPacketSender.playTransmissionParticle((ServerWorld) world, new TypedTransmission(pos, this.positionSource, delay, TypedTransmission.Variant.REDSTONE));
		}
	}
	
	public static class EventEntry {
		public RedstoneTransferGameEvent gameEvent;
		public int distance;
		
		public EventEntry(RedstoneTransferGameEvent gameEvent, int distance) {
			this.gameEvent = gameEvent;
			this.distance = distance;
		}
	}
	
}