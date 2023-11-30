package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.client.world.*;
import net.minecraft.util.*;
import net.minecraft.world.event.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class ColoredTransmissionParticle extends TransmissionParticle {
	
	public ColoredTransmissionParticle(ClientWorld world, double x, double y, double z, PositionSource positionSource, int maxAge, DyeColor dyeColor) {
		super(world, x, y, z, positionSource, maxAge);
		
		Vector3f color = ColorHelper.getRGBVec(dyeColor);
		this.setColor(color.x(), color.y(), color.z());
	}
	
}