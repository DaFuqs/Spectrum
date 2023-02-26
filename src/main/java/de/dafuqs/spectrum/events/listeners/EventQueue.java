package de.dafuqs.spectrum.events.listeners;

import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.event.listener.*;

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
	
	public void tick(World world) {
		if (!eventQueue.isEmpty()) {
			D[] keys = (D[]) eventQueue.keySet().toArray(); // protection from ConcurrentModificationExceptions
			for (D key : keys) {
				Integer tickCounter = eventQueue.get(key);
				if (tickCounter >= 1) {
					eventQueue.put(key, tickCounter - 1);
				} else {
					this.callback.triggerEvent(world, this, key);
					eventQueue.remove(key);
				}
			}
		}
	}

	public PositionSource getPositionSource() {
		return this.positionSource;
	}

	public int getRange() {
		return this.range;
	}

	public int getQueuedEventCount() {
		return this.eventQueue.size();
	}

	@Override
	public boolean listen(ServerWorld world, GameEvent.Message event) {
		Optional<Vec3d> positionSourcePosOptional = this.positionSource.getPos(world);
		if (positionSourcePosOptional.isEmpty()) {
			return false;
		} else {
			if (!this.callback.canAcceptEvent(world, this, event, positionSourcePosOptional.get())) {
				return false;
			} else {
				this.acceptEvent(world, event, positionSourcePosOptional.get());
				return true;
			}
		}
	}
	
	protected abstract void acceptEvent(World world, GameEvent.Message event, Vec3d sourcePos);
	
	protected void schedule(D object, int delay) {
		this.eventQueue.put(object, delay);
	}
	
	public interface Callback<D> {
		/**
		 * Returns whether the callback wants to accept this event.
		 */
		boolean canAcceptEvent(World world, GameEventListener listener, GameEvent.Message message, Vec3d sourcePos);
		
		/**
		 * Accepts a game event after delay.
		 */
		void triggerEvent(World world, GameEventListener listener, D entry);
	}
	
}