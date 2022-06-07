package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.ExperienceTransfer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;

public class ExperienceOrbEventQueue extends EventQueue<ExperienceOrbEventQueue.EventEntry> {
	
	public ExperienceOrbEventQueue(PositionSource positionSource, int range, Callback listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(World world, BlockPos pos, GameEvent event, Entity entity, BlockPos sourcePos) {
		if (world instanceof ServerWorld && entity instanceof ExperienceOrbEntity experienceOrbEntity) {
			EventEntry eventEntry = new EventEntry(event, experienceOrbEntity, MathHelper.floor(Math.sqrt(pos.getSquaredDistance(sourcePos)))); // copy
			int delay = eventEntry.distance * 2;
			this.schedule(eventEntry, delay);
			SpectrumS2CPacketSender.sendExperienceOrbTransferPacket((ServerWorld) world, new ExperienceTransfer(pos, this.positionSource, delay));
		}
	}
	
	public static class EventEntry {
		public GameEvent gameEvent;
		public ExperienceOrbEntity experienceOrbEntity;
		public int distance;
		
		public EventEntry(GameEvent gameEvent, ExperienceOrbEntity experienceOrbEntity, int distance) {
			this.gameEvent = gameEvent;
			this.experienceOrbEntity = experienceOrbEntity;
			this.distance = distance;
		}
	}
	
}