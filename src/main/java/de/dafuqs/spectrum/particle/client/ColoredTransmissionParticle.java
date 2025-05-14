package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.world.level.gameevent.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class ColoredTransmissionParticle extends TransmissionParticle {
	
	public ColoredTransmissionParticle(ClientLevel world, double x, double y, double z, PositionSource positionSource, int maxAge, int color) {
		super(world, x, y, z, positionSource, maxAge);
		
		Vector3f colorVec = SpectrumColorHelper.colorIntToVec(color);
		this.setColor(colorVec.x(), colorVec.y(), colorVec.z());
	}
	
}