package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class ItemEntityEventQueue extends EventQueue<ItemEntityEventQueue.EventEntry> {
	
	public ItemEntityEventQueue(PositionSource positionSource, int range, Callback listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(World world, GameEvent.Message event, Vec3d sourcePos) {
		if (world instanceof ServerWorld && event.getEmitter().sourceEntity() instanceof ItemEntity itemEntity) {
			Vec3d pos = event.getEmitterPos();
			EventEntry eventEntry = new EventEntry(event.getEvent(), itemEntity, MathHelper.floor(pos.distanceTo(sourcePos)));
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			SpectrumS2CPacketSender.playTransmissionParticle((ServerWorld) world, new TypedTransmission(pos, this.positionSource, delay, TypedTransmission.Variant.ITEM));
		}
	}
	
	public static class EventEntry {
		public final GameEvent gameEvent;
		public final ItemEntity itemEntity;
		public final int distance;
		
		public EventEntry(GameEvent gameEvent, ItemEntity itemEntity, int distance) {
			this.gameEvent = gameEvent;
			this.itemEntity = itemEntity;
			this.distance = distance;
		}
	}
	
}