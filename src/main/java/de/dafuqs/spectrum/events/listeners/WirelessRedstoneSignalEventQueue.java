package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.events.RedstoneTransferGameEvent;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.WirelessRedstoneTransmission;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;

public class WirelessRedstoneSignalEventQueue extends EventQueue<WirelessRedstoneSignalEventQueue.EventEntry> {
	
	public WirelessRedstoneSignalEventQueue(PositionSource positionSource, int range, EventQueue.Callback listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(World world, BlockPos pos, GameEvent event, Entity entity, BlockPos sourcePos) {
		if (world instanceof ServerWorld && event instanceof RedstoneTransferGameEvent redstoneTransferEvent) {
			WirelessRedstoneSignalEventQueue.EventEntry eventEntry = new WirelessRedstoneSignalEventQueue.EventEntry(redstoneTransferEvent, MathHelper.floor(Math.sqrt(pos.getSquaredDistance(sourcePos)))); // copy
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			SpectrumS2CPacketSender.sendWirelessRedstonePacket((ServerWorld) world, new WirelessRedstoneTransmission(pos, this.positionSource, delay));
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