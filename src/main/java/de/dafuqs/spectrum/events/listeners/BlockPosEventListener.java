package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.BlockPosEventTransfer;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockPosEventListener implements GameEventListener {
	
	protected final PositionSource positionSource;
	protected final int range;
	protected final BlockPosEventListener.Callback callback;
	protected Optional<GameEvent> event = Optional.empty();
	
	protected BlockPos eventSourceBlockPos;
	protected int distance;
	protected int delay = 0;
	
	public BlockPosEventListener(PositionSource positionSource, int range, BlockPosEventListener.Callback listener) {
		this.positionSource = positionSource;
		this.range = range;
		this.callback = listener;
	}
	
	public void tick(World world) {
		if (this.event.isPresent()) {
			--this.delay;
			if (this.delay <= 0) {
				this.delay = 0;
				this.callback.accept(world, this, this.event.get(), this.distance);
				this.event = Optional.empty();
			}
		}
	}
	
	public PositionSource getPositionSource() {
		return this.positionSource;
	}
	
	public int getRange() {
		return this.range;
	}
	
	public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
		if (!this.shouldActivate(event, entity)) {
			return false;
		} else {
			Optional<BlockPos> optional = this.positionSource.getPos(world);
			if (optional.isEmpty()) {
				return false;
			} else {
				BlockPos blockPos = optional.get();
				this.eventSourceBlockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ()); // copy
				if (!this.callback.accepts(world, this, pos, event, entity)) {
					return false;
				} else {
					this.listen(world, event, pos, blockPos);
					return true;
				}
			}
		}
	}
	
	boolean shouldActivate(GameEvent event, @Nullable Entity entity) {
		return true;
	}
	
	private void listen(World world, GameEvent event, BlockPos pos, BlockPos sourcePos) {
		this.event = Optional.of(event);
		if (world instanceof ServerWorld) {
			this.distance = MathHelper.floor(Math.sqrt(pos.getSquaredDistance(sourcePos)));
			this.delay = this.distance * 2;
			SpectrumS2CPacketSender.sendBlockPosEventTransferPacket((ServerWorld) world, new BlockPosEventTransfer(pos, this.positionSource, this.delay));
		}
	}
	
	public BlockPos getSourceBlockPos() {
		return this.eventSourceBlockPos;
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