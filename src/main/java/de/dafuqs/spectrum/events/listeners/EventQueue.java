package de.dafuqs.spectrum.events.listeners;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class EventQueue<D> implements GameEventListener {

	protected final PositionSource positionSource;
	protected final int range;
	protected final EventQueue.Callback callback;
	private final Map<D, Integer> eventQueue;

	public EventQueue(PositionSource positionSource, int range, EventQueue.Callback listener) {
		this.positionSource = positionSource;
		this.range = range;
		this.callback = listener;
		this.eventQueue = new HashMap<>();
	}

	public void tick(World world) {
		if (!eventQueue.isEmpty()) {
			// TODO: test for ConcurrentModificationExceptions
			D[] keys = (D[]) eventQueue.keySet().toArray();
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

	@Override
	public boolean listen(ServerWorld world, GameEvent.Message event) {
		Optional<Vec3d> positionSourcePosOptional = this.positionSource.getPos(world);
		if (positionSourcePosOptional.isEmpty()) {
			return false;
		} else {
			var entity = event.getEmitter().sourceEntity();
			var pos = new BlockPos(event.getEmitterPos());
			var sourcePos = new BlockPos(positionSourcePosOptional.get());
			if (!this.callback.canAcceptEvent(world, this, pos, event.getEvent(), entity, sourcePos)) {
				return false;
			} else {
				this.acceptEvent(world, pos, event.getEvent(), entity, sourcePos);
				return true;
			}
		}
	}

	protected abstract void acceptEvent(World world, BlockPos pos, GameEvent event, @Nullable Entity entity, BlockPos sourcePos);

	protected void schedule(D object, int delay) {
		this.eventQueue.put(object, delay);
	}

	public interface Callback<D> {
		/**
		 * Returns whether the callback wants to accept this event.
		 */
		boolean canAcceptEvent(World world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, BlockPos sourcePos);

		/**
		 * Accepts a game event after delay.
		 */
		void triggerEvent(World world, GameEventListener listener, D entry);
	}


}