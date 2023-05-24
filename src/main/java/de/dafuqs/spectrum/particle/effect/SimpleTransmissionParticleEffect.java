package de.dafuqs.spectrum.particle.effect;

import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
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
		Vec3d vec3d = this.destination.getPos(null).get();
		double d = vec3d.getX();
		double e = vec3d.getY();
		double f = vec3d.getZ();
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", Registries.PARTICLE_TYPE.getId(this.getType()), d, e, f, this.arrivalInTicks);
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
}
