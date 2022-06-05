package de.dafuqs.spectrum.events.listeners;

import blue.endless.jankson.annotation.Nullable;
import de.dafuqs.spectrum.events.RedstoneTransferGameEvent;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.WirelessRedstoneTransmission;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.Optional;

public class WirelessRedstoneSignalListener implements GameEventListener {
	
	protected final PositionSource positionSource;
	protected final int range;
	protected final WirelessRedstoneSignalListener.Callback callback;
	protected Optional<GameEvent> event = Optional.empty();
	protected int distance;
	protected int delay = 0;
	
	public WirelessRedstoneSignalListener(PositionSource positionSource, int range, WirelessRedstoneSignalListener.Callback listener) {
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
	
	@Override
	public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
		if (!this.shouldActivate(event, entity)) {
			return false;
		} else {
			Optional<BlockPos> optional = this.positionSource.getPos(world);
			if (optional.isEmpty()) {
				return false;
			} else {
				BlockPos blockPos = optional.get();
				if (!this.callback.accepts(world, this, pos, event, entity)) {
					return false;
				} else {
					this.listen(world, event, pos, blockPos);
					return true;
				}
			}
		}
	}
	
	private boolean shouldActivate(GameEvent event, @Nullable Entity entity) {
		return (this.event.isEmpty() && event instanceof RedstoneTransferGameEvent);
	}
	
	private void listen(World world, GameEvent event, BlockPos pos, BlockPos sourcePos) {
		this.event = Optional.of(event);
		if (world instanceof ServerWorld) {
			this.distance = MathHelper.floor(Math.sqrt(pos.getSquaredDistance(sourcePos)));
			this.delay = this.distance;
			SpectrumS2CPacketSender.sendWirelessRedstonePacket((ServerWorld) world, new WirelessRedstoneTransmission(pos, this.positionSource, this.delay));
		}
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