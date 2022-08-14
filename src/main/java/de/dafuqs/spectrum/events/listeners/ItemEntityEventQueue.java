package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.ItemTransfer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;

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
			SpectrumS2CPacketSender.sendItemTransferPacket((ServerWorld) world, new ItemTransfer(pos, this.positionSource, delay));
		}
	}
	
	public static class EventEntry {
		public GameEvent gameEvent;
		public ItemEntity itemEntity;
		public int distance;
		
		public EventEntry(GameEvent gameEvent, ItemEntity itemEntity, int distance) {
			this.gameEvent = gameEvent;
			this.itemEntity = itemEntity;
			this.distance = distance;
		}
	}
	
}