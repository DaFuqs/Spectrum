package de.dafuqs.spectrum.particle.effect;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

import java.util.Locale;

public abstract class TransmissionParticleEffect implements ParticleEffect {
	
	protected final PositionSource destination;
	protected final int arrivalInTicks;
	
	public TransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks) {
		this.destination = positionSource;
		this.arrivalInTicks = arrivalInTicks;
	}
	
	public void write(PacketByteBuf buf) {
		PositionSourceType.write(this.destination, buf);
		buf.writeVarInt(this.arrivalInTicks);
	}
	
	public String asString() {
		Vec3d vec3d = this.destination.getPos(null).get();
		double d = vec3d.getX();
		double e = vec3d.getY();
		double f = vec3d.getZ();
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", Registry.PARTICLE_TYPE.getId(this.getType()), d, e, f, this.arrivalInTicks);
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
}
