package de.dafuqs.spectrum.events.listeners;

import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;

public abstract class EventQueue<D> implements GameEventListener {
	
	protected final PositionSource positionSource;
	protected final int range;
	protected final EventQueue.Callback<D> callback;
	protected final Map<D, Integer> eventQueue;
	
	public EventQueue(PositionSource positionSource, int range, EventQueue.Callback<D> listener) {
		this.positionSource = positionSource;
		this.range = range;
		this.callback = listener;
		this.eventQueue = new HashMap<>();
	}
	
	public void tick(Level world) {
		for (D key : new HashSet<>(eventQueue.keySet())) {
			Integer tickCounter = eventQueue.get(key);
			if (tickCounter >= 1) {
				eventQueue.put(key, tickCounter - 1);
			} else {
				this.callback.triggerEvent(world, this, key);
				eventQueue.remove(key);
			}
		}
	}
	
	@Override
	public PositionSource getListenerSource() {
		return this.positionSource;
	}
	
	@Override
	public int getListenerRadius() {
		return this.range;
	}

	public int getQueuedEventCount() {
		return this.eventQueue.size();
	}

	@Override
	public boolean handleGameEvent(ServerLevel world, Holder<GameEvent> event, GameEvent.Context emitter, Vec3 emitterPos) {
		Optional<Vec3> positionSourcePosOptional = this.positionSource.getPosition(world);
		if (positionSourcePosOptional.isEmpty()) {
			return false;
		} else {
			if (!this.callback.canAcceptEvent(world, this, new GameEvent.ListenerInfo(event, emitterPos, emitter, this, positionSourcePosOptional.get()), positionSourcePosOptional.get())) {
				return false;
			} else {
				this.acceptEvent(world, new GameEvent.ListenerInfo(event, emitterPos, emitter, this, positionSourcePosOptional.get()), positionSourcePosOptional.get());
				return true;
			}
		}
	}
	
	protected abstract void acceptEvent(Level world, GameEvent.ListenerInfo event, Vec3 sourcePos);
	
	protected void schedule(D object, int delay) {
		this.eventQueue.put(object, delay);
	}
	
	public interface Callback<D> {
		/**
		 * Returns whether the callback wants to accept this event.
		 */
		boolean canAcceptEvent(Level world, GameEventListener listener, GameEvent.ListenerInfo message, Vec3 sourcePos);
		
		/**
		 * Accepts a game event after delay.
		 */
		void triggerEvent(Level world, GameEventListener listener, D entry);
	}
	
}