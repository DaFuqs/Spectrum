package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.particle.ParticlePattern;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ParticleHelper {
	
	public static void playParticleWithPatternAndVelocityClient(World world, Vec3d position, ParticleEffect particleEffect, @NotNull ParticlePattern pattern, double velocity) {
		for (Vec3d vec3d : pattern.getVectors()) {
			world.addParticle(particleEffect, position.getX(), position.getY(), position.getZ(), vec3d.x * velocity, vec3d.y * velocity, vec3d.z * velocity);
		}
	}
	
}
