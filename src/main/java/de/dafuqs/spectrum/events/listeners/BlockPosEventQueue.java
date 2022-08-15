package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.BlockPosEventTransfer;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;

public class BlockPosEventQueue extends EventQueue<BlockPosEventQueue.EventEntry> {
	
	public BlockPosEventQueue(PositionSource positionSource, int range, Callback listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(World world, GameEvent.Message event, Vec3d sourcePos) {
		if (world instanceof ServerWorld) {
			Vec3d emitterPos = event.getEmitterPos();
			EventEntry eventEntry = new EventEntry(event.getEvent(), new BlockPos(emitterPos.x, emitterPos.y, emitterPos.z), MathHelper.floor(event.getEmitterPos().distanceTo(sourcePos)));
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			SpectrumS2CPacketSender.sendBlockPosEventTransferPacket((ServerWorld) world, new BlockPosEventTransfer(emitterPos, this.positionSource, delay));
		}
	}
	
	public static class EventEntry {
		public GameEvent gameEvent;
		public BlockPos eventSourceBlockPos;
		public int distance;
		
		public EventEntry(GameEvent gameEvent, BlockPos eventSourceBlockPos, int distance) {
			this.gameEvent = gameEvent;
			this.eventSourceBlockPos = eventSourceBlockPos;
			this.distance = distance;
		}
	}
	
}