package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.events.*;
import net.minecraft.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.event.listener.*;

/**
 * Since Sucking chests can react to both spawned items and experience
 * this class is a wrapper around those two
 * (Each Block can only ever have a single event listener)
 */
public class ItemAndExperienceEventQueue implements GameEventListener {
	
	public final ItemEntityEventListener itemListener;
	public final ExperienceOrbEventListener experienceListener;
	
	public ItemAndExperienceEventQueue(PositionSource positionSource, int range, EventQueue.Callback<Object> listener) {
		this.itemListener = new ItemEntityEventListener(positionSource, range, listener);
		this.experienceListener = new ExperienceOrbEventListener(positionSource, range, listener);
	}
	
	@Override
	public PositionSource getPositionSource() {
		return this.itemListener.eventQueue.getPositionSource();
	}
	
	@Override
	public int getRange() {
		return this.itemListener.eventQueue.getRange();
	}
	
	@Override
	public boolean listen(ServerWorld world, GameEvent.Message event) {
		if (event.getEvent() != SpectrumGameEvents.ENTITY_SPAWNED) {
			return false;
		}
		
		Entity entity = event.getEmitter().sourceEntity();
		
		return (
			entity instanceof ItemEntity && itemListener.eventQueue.listen(world, event) ||
			entity instanceof ExperienceOrbEntity && experienceListener.eventQueue.listen(world, event)
		);
	}
	
	public void tick(World world) {
		this.itemListener.eventQueue.tick(world);
		this.experienceListener.eventQueue.tick(world);
	}
	
	public static class ItemEntityEventListener implements EventQueue.Callback<ItemEntityEventQueue.EventEntry> {
		public final EventQueue.Callback<Object> parentListener;
		public final ItemEntityEventQueue eventQueue;
		
		public ItemEntityEventListener(PositionSource positionSource, int range, EventQueue.Callback<Object> listener) {
			this.parentListener = listener;
			this.eventQueue = new ItemEntityEventQueue(positionSource, range, this);
		}
		
		@Override
		public boolean canAcceptEvent(World world, GameEventListener listener, GameEvent.Message event, Vec3d sourcePos) {
			return this.parentListener.canAcceptEvent(world, listener, event, sourcePos);
		}
		
		@Override
		public void triggerEvent(World world, GameEventListener listener, ItemEntityEventQueue.EventEntry entry) {
			this.parentListener.triggerEvent(world, listener, entry);
		}
	}
	
	public static class ExperienceOrbEventListener implements EventQueue.Callback<ExperienceOrbEventQueue.EventEntry> {
		public final EventQueue.Callback<Object> parentListener;
		public final ExperienceOrbEventQueue eventQueue;
		
		public ExperienceOrbEventListener(PositionSource positionSource, int range, EventQueue.Callback<Object> listener) {
			this.parentListener = listener;
			this.eventQueue = new ExperienceOrbEventQueue(positionSource, range, this);
		}
		
		@Override
		public boolean canAcceptEvent(World world, GameEventListener listener, GameEvent.Message event, Vec3d sourcePos) {
			return this.parentListener.canAcceptEvent(world, listener, event, sourcePos);
		}
		
		@Override
		public void triggerEvent(World world, GameEventListener listener, ExperienceOrbEventQueue.EventEntry entry) {
			this.parentListener.triggerEvent(world, listener, entry);
		}
	}
	
}
