package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;

public record MemoryComponent(int ticksToManifest, boolean spawnAsAdult, boolean brokenPromise, boolean unrecognizable) {
	
	// zero or negative ticks to manifest: never hatch
	
	public static final MemoryComponent DEFAULT = new MemoryComponent(-1, false, false, false);
	
	public static final Codec<MemoryComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("ticks_to_manifest", -1).forGetter(MemoryComponent::ticksToManifest),
			Codec.BOOL.optionalFieldOf("spawn_as_adult", false).forGetter(MemoryComponent::spawnAsAdult),
			Codec.BOOL.optionalFieldOf("broken_promise", false).forGetter(MemoryComponent::brokenPromise),
			Codec.BOOL.optionalFieldOf("unrecognizable", false).forGetter(MemoryComponent::unrecognizable)
	).apply(i, MemoryComponent::new));
	
	public static final StreamCodec<ByteBuf, MemoryComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, MemoryComponent::ticksToManifest,
			ByteBufCodecs.BOOL, MemoryComponent::spawnAsAdult,
			ByteBufCodecs.BOOL, MemoryComponent::brokenPromise,
			ByteBufCodecs.BOOL, MemoryComponent::unrecognizable,
			MemoryComponent::new
	);
	
	public static class Builder {
		private int ticksToManifest;
		private boolean spawnAsAdult;
		private boolean brokenPromise;
		private boolean unrecognizable;
		
		public Builder(MemoryComponent component) {
			this.ticksToManifest = component.ticksToManifest;
			this.spawnAsAdult = component.spawnAsAdult;
			this.brokenPromise = component.brokenPromise;
			this.unrecognizable = component.unrecognizable;
		}
		
		public Builder ticksToManifest(int ticksToManifest) {
			this.ticksToManifest = ticksToManifest;
			return this;
		}
		
		public Builder spawnAsAdult(boolean spawnAsAdult) {
			this.spawnAsAdult = spawnAsAdult;
			return this;
		}
		
		public Builder brokenPromise(boolean brokenPromise) {
			this.brokenPromise = brokenPromise;
			return this;
		}
		
		public Builder unrecognizable() {
			this.unrecognizable = true;
			return this;
		}
		
		public MemoryComponent build() {
			return new MemoryComponent(this.ticksToManifest, this.spawnAsAdult, this.brokenPromise, this.unrecognizable);
		}
		
	}
	
}
