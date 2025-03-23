package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.particle.*;
import net.minecraft.block.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ParticleHelper {
	
	public static void playParticleWithPatternAndVelocityClient(World world, Vec3d position, ParticleEffect particleEffect, @NotNull VectorPattern pattern, double velocity) {
		for (Vec3d vec3d : pattern.getVectors()) {
			world.addParticle(particleEffect, position.getX(), position.getY(), position.getZ(), vec3d.x * velocity, vec3d.y * velocity, vec3d.z * velocity);
		}
	}

	public static void playParticleWithRotation(World world, Vec3d position, double longitude, double latitude, ParticleEffect particleEffect, @NotNull VectorPattern pattern, double velocity) {
		for (Vec3d vec3d : pattern.getVectors()) {
			var length = vec3d.length();
			var orientation = Orientation.getVectorOrientation(vec3d).add(longitude, latitude);
			vec3d = orientation.toVector(length);

			world.addParticle(particleEffect, position.getX(), position.getY(), position.getZ(), vec3d.x * velocity, vec3d.y * velocity, vec3d.z * velocity);
		}
	}
	
	public static void playTriangulatedParticle(World world, ParticleEffect particleEffect, int quantity, boolean triangular, Vec3d scale, double bonusYOffset, boolean solidSpawns, Vec3d position, Vec3d velocity) {
		var random = world.getRandom();
		
		for (int i = 0; i < quantity; i++) {
			
			double d;
			double e;
			double f;
			
			if (triangular) {
				d = random.nextTriangular(0, scale.x);
				e = random.nextTriangular(0, scale.y) + bonusYOffset;
				f = random.nextTriangular(0, scale.z);
			} else {
				d = random.nextDouble() * 2 * scale.x - scale.x;
				e = random.nextDouble() * 2 * scale.y - scale.y + -bonusYOffset;
				f = random.nextDouble() * 2 * scale.z - scale.z;
			}
			
			if (!solidSpawns && world.isAir(BlockPos.ofFloored(position))) {
				continue;
			}
			
			world.addParticle(particleEffect, position.getX() + d, position.getY() + e, position.getZ() + f, velocity.getX(), velocity.getY(), velocity.getZ());
		}
	}
	
	public static void playParticleAroundBlockSides(World world, ParticleEffect particleEffect, Vec3d position, Direction[] sides, int quantity, Vec3d velocity) {
		var random = world.getRandom();
		var basePos = BlockPos.ofFloored(position);
		
		for (Direction direction : sides) {
			BlockPos blockPos = basePos.offset(direction);
			BlockState state = world.getBlockState(blockPos);
			if (state.isOpaque() && state.isSideSolidFullSquare(world, blockPos, direction.getOpposite()))
				continue;
			
			for (int i = 0; i < quantity; i++) {
				double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetX() * 0.6D;
				double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetY() * 0.6D;
				double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetZ() * 0.6D;
				world.addParticle(particleEffect, position.getX() + d, position.getY() + e, position.getZ() + f, velocity.getX(), velocity.getY(), velocity.getZ());
			}
		}
	}
}
