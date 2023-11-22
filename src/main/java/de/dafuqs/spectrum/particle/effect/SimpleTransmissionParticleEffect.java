package de.dafuqs.spectrum.particle.effect;

import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

import java.util.*;

public abstract class SimpleTransmissionParticleEffect implements ParticleEffect {
	
	protected final PositionSource destination;
	protected final int arrivalInTicks;
	
	public SimpleTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks) {
		this.destination = positionSource;
		this.arrivalInTicks = arrivalInTicks;
	}
	
	@Override
	public void write(PacketByteBuf buf) {
		PositionSourceType.write(this.destination, buf);
		buf.writeVarInt(this.arrivalInTicks);
	}
	
	@Override
	public String asString() {
		Optional<Vec3d> pos = this.destination.getPos(null);
		if (pos.isPresent()) {
			double d = pos.get().getX();
			double e = pos.get().getY();
			double f = pos.get().getZ();
			return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", Registries.PARTICLE_TYPE.getId(this.getType()), d, e, f, this.arrivalInTicks);
		}
		return String.format(Locale.ROOT, "%s <no destination> %d", Registries.PARTICLE_TYPE.getId(this.getType()), this.arrivalInTicks);
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
}
