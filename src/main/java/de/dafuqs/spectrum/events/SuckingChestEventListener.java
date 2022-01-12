package de.dafuqs.spectrum.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

/**
 * Since Sucking chests can react to both spawned items and experience
 * this class is a wrapper around those two
 */
public class SuckingChestEventListener implements GameEventListener, ItemEntityTransferListener.Callback, ExperienceOrbEntityTransferListener.Callback {
	
	SuckingChestEventListener.Callback listener;
	private final ItemEntityTransferListener itemEntityTransferListener;
	private final ExperienceOrbEntityTransferListener experienceTransferListener;
	
	public SuckingChestEventListener(PositionSource positionSource, int range, SuckingChestEventListener.Callback listener) {
		this.listener = listener;
		this.itemEntityTransferListener = new ItemEntityTransferListener(positionSource, range, this);
		this.experienceTransferListener = new ExperienceOrbEntityTransferListener(positionSource, range, this);
	}
	
	@Override
	public PositionSource getPositionSource() {
		return this.itemEntityTransferListener.getPositionSource();
	}
	
	@Override
	public int getRange() {
		return this.itemEntityTransferListener.getRange();
	}
	
	@Override
	public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
		if (!itemEntityTransferListener.shouldActivate(event, entity)) {
			if(!experienceTransferListener.shouldActivate(event, entity)) {
				return false;
			} else {
				return experienceTransferListener.listen(world, event, entity, pos);
			}
		} else {
			return itemEntityTransferListener.listen(world, event, entity, pos);
		}
	}
	
	@Override
	public boolean accepts(World world, GameEventListener listener, BlockPos pos, GameEvent event, Entity entity) {
		return this.listener.accepts(world, listener, pos, event, entity);
	}
	
	@Override
	public void accept(World world, GameEventListener listener, GameEvent event, int distance) {
		this.listener.accept(world, listener, event, distance);
	}
	
	public void tick(World world) {
		this.itemEntityTransferListener.tick(world);
		this.experienceTransferListener.tick(world);
	}
	
	public interface Callback {
		/**
		 * Returns whether the callback wants to accept this event.
		 */
		boolean accepts(World world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity);
		
		/**
		 * Accepts a game event after delay.
		 */
		void accept(World world, GameEventListener listener, GameEvent event, int distance);
	}
	
}
