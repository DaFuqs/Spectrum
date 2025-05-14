package de.dafuqs.spectrum.particle.effect;

import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public abstract class SimpleTransmission {
	
	protected final Vec3 origin;
	protected final PositionSource destination;
	protected final int arrivalInTicks;
	
	public SimpleTransmission(Vec3 origin, PositionSource destination, int arrivalInTicks) {
		this.origin = origin;
		this.destination = destination;
		this.arrivalInTicks = arrivalInTicks;
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
	public Vec3 getOrigin() {
		return this.origin;
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
	
}
