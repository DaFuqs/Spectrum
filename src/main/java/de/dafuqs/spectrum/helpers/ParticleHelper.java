package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class ParticleHelper {
	
	public static void playParticleWithPatternAndVelocityClient(Level world, Vec3 position, ParticleOptions particleEffect, VectorPattern pattern, double velocity) {
		for (Vec3 vec3d : pattern.getVectors()) {
			world.addParticle(particleEffect, position.x(), position.y(), position.z(), vec3d.x * velocity, vec3d.y * velocity, vec3d.z * velocity);
		}
	}
	
	public static void playParticleWithRotation(Level world, Vec3 position, double longitude, double latitude, ParticleOptions particleEffect, VectorPattern pattern, double velocity) {
		for (Vec3 vec3d : pattern.getVectors()) {
			var length = vec3d.length();
			var orientation = Orientation.getVectorOrientation(vec3d).add(longitude, latitude);
			vec3d = orientation.toVector(length);
			
			world.addParticle(particleEffect, position.x(), position.y(), position.z(), vec3d.x * velocity, vec3d.y * velocity, vec3d.z * velocity);
		}
	}
	
	public static void playTriangulatedParticle(Level world, ParticleOptions particleEffect, int quantity, boolean triangular, Vec3 scale, double bonusYOffset, boolean solidSpawns, Vec3 position, Vec3 velocity) {
		var random = world.getRandom();
		
		for (int i = 0; i < quantity; i++) {
			
			double d;
			double e;
			double f;
			
			if (triangular) {
				d = random.triangle(0, scale.x);
				e = random.triangle(0, scale.y) + bonusYOffset;
				f = random.triangle(0, scale.z);
			} else {
				d = random.nextDouble() * 2 * scale.x - scale.x;
				e = random.nextDouble() * 2 * scale.y - scale.y + -bonusYOffset;
				f = random.nextDouble() * 2 * scale.z - scale.z;
			}
			
			if (!solidSpawns && world.isEmptyBlock(BlockPos.containing(position))) {
				continue;
			}
			
			world.addParticle(particleEffect, position.x() + d, position.y() + e, position.z() + f, velocity.x(), velocity.y(), velocity.z());
		}
	}
	
	public static void playParticleAroundBlockSides(Level world, ParticleOptions particleEffect, BlockPos position, Direction[] sides, int quantity, Vec3 velocity) {
		playParticleAroundBlockSides(world, particleEffect, position, List.of(sides), quantity, velocity);
	}
	
	public static void playParticleAroundBlockSides(Level world, ParticleOptions particleEffect, BlockPos position, List<Direction> sides, int quantity, Vec3 velocity) {
		var random = world.getRandom();
		
		for (Direction direction : sides) {
			BlockPos blockPos = position.relative(direction);
			BlockState state = world.getBlockState(blockPos);
			if (state.canOcclude() && state.isFaceSturdy(world, blockPos, direction.getOpposite()))
				continue;
			
			for (int i = 0; i < quantity; i++) {
				double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepX() * 0.6D;
				double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepY() * 0.6D;
				double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepZ() * 0.6D;
				world.addParticle(particleEffect, position.getX() + d, position.getY() + e, position.getZ() + f, velocity.x(), velocity.y(), velocity.z());
			}
		}
	}
}
