package de.dafuqs.spectrum.particle.effect;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class TransmissionParticleEffect implements ParticleOptions {
	
	public static final MapCodec<TransmissionParticleEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("particle_type").forGetter(c -> c.particleType),
			PositionSource.CODEC.fieldOf("destination").forGetter(c -> c.destination),
			Codec.INT.fieldOf("arrival_in_ticks").forGetter(c -> c.arrivalInTicks)
	).apply(i, TransmissionParticleEffect::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, TransmissionParticleEffect> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.PARTICLE_TYPE), c -> c.particleType,
			PositionSource.STREAM_CODEC, c -> c.destination,
			ByteBufCodecs.VAR_INT, c -> c.arrivalInTicks,
			TransmissionParticleEffect::new
	);
	
	protected final ParticleType<?> particleType;
	protected final PositionSource destination;
	protected final int arrivalInTicks;
	
	public TransmissionParticleEffect(ParticleType<?> particleType, PositionSource positionSource, int arrivalInTicks) {
		this.particleType = particleType;
		this.destination = positionSource;
		this.arrivalInTicks = arrivalInTicks;
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
	@Override
	public ParticleType<?> getType() {
		return particleType;
	}
	
	@Override
	public String toString() {
		Optional<Vec3> pos = this.destination.getPosition(null);
		if (pos.isPresent()) {
			double d = pos.get().x();
			double e = pos.get().y();
			double f = pos.get().z();
			return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), d, e, f, this.arrivalInTicks);
		}
		return String.format(Locale.ROOT, "%s <no destination> %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.arrivalInTicks);
	}
}
