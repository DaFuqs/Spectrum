package de.dafuqs.spectrum.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public abstract class QueuedEventTransferListener<D> implements GameEventListener {

	protected final PositionSource positionSource;
	protected final int range;
	protected final QueuedEventTransferListener.Callback callback;
	private final Map<D, Integer> eventQueue;
	private boolean frozen = false; // protect against ConcurrentModificationExceptions

	public QueuedEventTransferListener(PositionSource positionSource, int range, QueuedEventTransferListener.Callback listener) {
		this.positionSource = positionSource;
		this.range = range;
		this.callback = listener;
		this.eventQueue = new HashMap<>();
	}

	public void tick(World world) {
		if(!eventQueue.isEmpty()) {
			frozen = true;
			Iterator<Map.Entry<D, Integer>> iterator = eventQueue.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<D, Integer> next = iterator.next();
				D eventEntry = next.getKey();
				
				if (next.getValue() >= 1) {
					next.setValue(next.getValue()-1);
				} else {
					this.callback.triggerEvent(world, this, eventEntry);
					iterator.remove();
				}
			}
			frozen = false;
		}
	}

	public PositionSource getPositionSource() {
		return this.positionSource;
	}

	public int getRange() {
		return this.range;
	}
	
	public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
		Optional<BlockPos> positionSourcePosOptional = this.positionSource.getPos(world);
		if (frozen || positionSourcePosOptional.isEmpty()) {
			return false;
		} else {
			if (!this.callback.acceptsEvent(world, this, pos, event, positionSourcePosOptional.get())) {
				return false;
			} else {
				this.acceptEvent(world, pos, event, positionSourcePosOptional.get());
				return true;
			}
		}
	}
	
	protected abstract void acceptEvent(World world, BlockPos pos, GameEvent event, BlockPos sourcePos);
	
	protected void schedule(D object, int delay) {
		if(!frozen) {
			this.eventQueue.put(object, delay);
		}
	}
	
	public interface Callback<D> {
		/**
		 * Returns whether the callback wants to accept this event.
		 */
		boolean acceptsEvent(World world, GameEventListener listener, BlockPos pos, GameEvent event, BlockPos sourcePos);

		/**
		 * Accepts a game event after delay.
		 */
		void triggerEvent(World world, GameEventListener listener, D entry);
	}

}