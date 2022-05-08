package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.BlockPosEventTransfer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;

public class QueuedBlockPosEventTransferListener extends QueuedEventTransferListener<QueuedBlockPosEventTransferListener.BlockPosEventEntry> {
	
	public static class BlockPosEventEntry {
		public GameEvent gameEvent;
		public BlockPos eventSourceBlockPos;
		public int distance;
		
		public BlockPosEventEntry(GameEvent gameEvent, BlockPos eventSourceBlockPos, int distance) {
			this.gameEvent = gameEvent;
			this.eventSourceBlockPos = eventSourceBlockPos;
			this.distance = distance;
		}
	}
	
	public QueuedBlockPosEventTransferListener(PositionSource positionSource, int range, Callback listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(World world, BlockPos pos, GameEvent event, BlockPos sourcePos) {
		if (world instanceof ServerWorld) {
			QueuedBlockPosEventTransferListener.BlockPosEventEntry eventEntry = new QueuedBlockPosEventTransferListener.BlockPosEventEntry(event, new BlockPos(pos.getX(), pos.getY(), pos.getZ()), MathHelper.floor(Math.sqrt(pos.getSquaredDistance(sourcePos)))); // copy
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			SpectrumS2CPacketSender.sendBlockPosEventTransferPacket((ServerWorld) world, new BlockPosEventTransfer(pos, this.positionSource, delay));
		}
	}
	
}