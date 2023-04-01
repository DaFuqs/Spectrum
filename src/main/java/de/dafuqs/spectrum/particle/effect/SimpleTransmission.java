package de.dafuqs.spectrum.particle.effect;

import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

public abstract class SimpleTransmission {
	
	protected final Vec3d origin;
	protected final PositionSource destination;
	protected final int arrivalInTicks;
	
	public SimpleTransmission(Vec3d origin, PositionSource destination, int arrivalInTicks) {
		this.origin = origin;
		this.destination = destination;
		this.arrivalInTicks = arrivalInTicks;
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
	public Vec3d getOrigin() {
		return this.origin;
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
	
}
