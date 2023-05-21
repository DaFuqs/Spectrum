package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class BlockPosEventQueue extends EventQueue<BlockPosEventQueue.EventEntry> {
	
	public BlockPosEventQueue(PositionSource positionSource, int range, Callback<EventEntry> listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(World world, GameEvent.Message event, Vec3d sourcePos) {
		if (world instanceof ServerWorld) {
			Vec3d emitterPos = event.getEmitterPos();
			EventEntry eventEntry = new EventEntry(event.getEvent(), BlockPos.ofFloored(emitterPos.x, emitterPos.y, emitterPos.z), MathHelper.floor(event.getEmitterPos().distanceTo(sourcePos)));
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			SpectrumS2CPacketSender.playTransmissionParticle((ServerWorld) world, new TypedTransmission(emitterPos, this.positionSource, delay, TypedTransmission.Variant.BLOCK_POS));
		}
	}
	
	public static class EventEntry {
		public final GameEvent gameEvent;
		public final BlockPos eventSourceBlockPos;
		public final int distance;
		
		public EventEntry(GameEvent gameEvent, BlockPos eventSourceBlockPos, int distance) {
			this.gameEvent = gameEvent;
			this.eventSourceBlockPos = eventSourceBlockPos;
			this.distance = distance;
		}
	}
	
}