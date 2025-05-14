package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.events.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

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
	public PositionSource getListenerSource() {
		return this.itemListener.eventQueue.getListenerSource();
	}
	
	@Override
	public int getListenerRadius() {
		return this.itemListener.eventQueue.getListenerRadius();
	}
	
	@Override
	public boolean handleGameEvent(ServerLevel world, Holder<GameEvent> event, GameEvent.Context emitter, Vec3 emitterPos) {
		if (event != SpectrumGameEvents.ENTITY_SPAWNED) {
			return false;
		}

		Entity entity = emitter.sourceEntity();
		
		return (entity instanceof ItemEntity && itemListener.eventQueue.handleGameEvent(world, event, emitter, emitterPos)
				|| entity instanceof ExperienceOrb && experienceListener.eventQueue.handleGameEvent(world, event, emitter, emitterPos));
	}
	
	public void tick(Level world) {
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
		public boolean canAcceptEvent(Level world, GameEventListener listener, GameEvent.ListenerInfo event, Vec3 sourcePos) {
			return this.parentListener.canAcceptEvent(world, listener, event, sourcePos);
		}

		@Override
		public void triggerEvent(Level world, GameEventListener listener, ItemEntityEventQueue.EventEntry entry) {
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
		public boolean canAcceptEvent(Level world, GameEventListener listener, GameEvent.ListenerInfo event, Vec3 sourcePos) {
			return this.parentListener.canAcceptEvent(world, listener, event, sourcePos);
		}

		@Override
		public void triggerEvent(Level world, GameEventListener listener, ExperienceOrbEventQueue.EventEntry entry) {
			this.parentListener.triggerEvent(world, listener, entry);
		}
	}
	
}
