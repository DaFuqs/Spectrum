package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.events.SpectrumGameEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

/**
 * Since Sucking chests can react to both spawned items and experience
 * this class is a wrapper around those two
 * (Each Block can only ever have a single event listener)
 */
public class ItemAndExperienceEventQueue implements GameEventListener {
	
	protected final EventQueue.Callback listener;
	protected final ItemEntityEventQueue itemQueue;
	protected final ExperienceOrbEventQueue experienceQueue;
	
	public ItemAndExperienceEventQueue(PositionSource positionSource, int range, EventQueue.Callback listener) {
		this.listener = listener;
		this.itemQueue = new ItemEntityEventQueue(positionSource, range, listener);
		this.experienceQueue = new ExperienceOrbEventQueue(positionSource, range, listener);
	}
	
	@Override
	public PositionSource getPositionSource() {
		return this.itemQueue.getPositionSource();
	}
	
	@Override
	public int getRange() {
		return this.itemQueue.getRange();
	}
	
	@Override
	public boolean listen(ServerWorld world, GameEvent.Message event) {
		if (event.getEvent() != SpectrumGameEvents.ENTITY_SPAWNED) {
			return false;
		}
		Entity entity = event.getEmitter().sourceEntity();
		if (entity instanceof ItemEntity && itemQueue.listen(world, event)) {
			return true;
		}
		return entity instanceof ExperienceOrbEntity && experienceQueue.listen(world, event);
	}
	
	public void tick(World world) {
		this.itemQueue.tick(world);
		this.experienceQueue.tick(world);
	}
	
}
