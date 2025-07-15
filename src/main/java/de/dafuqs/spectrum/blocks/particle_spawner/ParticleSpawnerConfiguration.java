package de.dafuqs.spectrum.blocks.particle_spawner;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public record ParticleSpawnerConfiguration(
		ParticleType<?> particleType,
		Vec3i cmyColor, // 0-100 cmy
		boolean glowing,
		float particlesPerSecond, /* >1 = every xth tick */
		Vec3 sourcePosition,
		Vec3 sourcePositionVariance,
		Vec3 velocity,
		Vec3 velocityVariance,
		float scale,
		float scaleVariance,
		int lifetimeTicks,
		int lifetimeVariance,
		float gravity,
		boolean collisions
) {
	
	public static final Codec<ParticleSpawnerConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("particle_type_identifier").forGetter(ParticleSpawnerConfiguration::particleType),
			Vec3i.CODEC.fieldOf("color").forGetter(ParticleSpawnerConfiguration::cmyColor),
			Codec.BOOL.fieldOf("glowing").forGetter(ParticleSpawnerConfiguration::glowing),
			Codec.FLOAT.fieldOf("particles_per_tick").forGetter(ParticleSpawnerConfiguration::particlesPerSecond),
			Vec3.CODEC.fieldOf("source_pos").forGetter(ParticleSpawnerConfiguration::sourcePosition),
			Vec3.CODEC.fieldOf("source_pos_variance").forGetter(ParticleSpawnerConfiguration::sourcePositionVariance),
			Vec3.CODEC.fieldOf("source_velocity").forGetter(ParticleSpawnerConfiguration::velocity),
			Vec3.CODEC.fieldOf("source_velocity_variance").forGetter(ParticleSpawnerConfiguration::velocityVariance),
			Codec.FLOAT.fieldOf("scale").forGetter(ParticleSpawnerConfiguration::scale),
			Codec.FLOAT.fieldOf("scale_variance").forGetter(ParticleSpawnerConfiguration::scaleVariance),
			Codec.INT.fieldOf("lifetime").forGetter(ParticleSpawnerConfiguration::lifetimeTicks),
			Codec.INT.fieldOf("lifetime_variance").forGetter(ParticleSpawnerConfiguration::lifetimeVariance),
			Codec.FLOAT.fieldOf("gravity").forGetter(ParticleSpawnerConfiguration::gravity),
			Codec.BOOL.fieldOf("collisions").forGetter(ParticleSpawnerConfiguration::collisions)
	).apply(i, ParticleSpawnerConfiguration::new));
	
	public static final StreamCodec<FriendlyByteBuf, ParticleSpawnerConfiguration> PACKET_CODEC = PacketCodecHelper.tuple(
			PacketCodecHelper.registryValueByName(BuiltInRegistries.PARTICLE_TYPE), c -> c.particleType,
			PacketCodecHelper.VEC3I, c -> c.cmyColor,
			ByteBufCodecs.BOOL, c -> c.glowing,
			ByteBufCodecs.FLOAT, c -> c.particlesPerSecond,
			PacketCodecHelper.VEC3D, c -> c.sourcePosition,
			PacketCodecHelper.VEC3D, c -> c.sourcePositionVariance,
			PacketCodecHelper.VEC3D, c -> c.velocity,
			PacketCodecHelper.VEC3D, c -> c.velocityVariance,
			ByteBufCodecs.FLOAT, c -> c.scale,
			ByteBufCodecs.FLOAT, c -> c.scaleVariance,
			ByteBufCodecs.VAR_INT, c -> c.lifetimeTicks,
			ByteBufCodecs.VAR_INT, c -> c.lifetimeVariance,
			ByteBufCodecs.FLOAT, c -> c.gravity,
			ByteBufCodecs.BOOL, c -> c.collisions,
			ParticleSpawnerConfiguration::new
	);
	
	public static Vector3fc CMYtoRGB(Vec3i cmy) {
		float r = 1F - cmy.getX() / 100F;
		float g = 1F - cmy.getY() / 100F;
		float b = 1F - cmy.getZ() / 100F;
		return new Vector3f(r, g, b);
	}
	
	public void spawnParticles(Level world, @NotNull BlockPos pos) {
		float particlesToSpawn = particlesPerSecond / 20F;
		while (particlesToSpawn >= 1 || world.random.nextFloat() < particlesToSpawn) {
			spawnParticle(world, pos, world.random);
			particlesToSpawn--;
		}
	}
	
	private void spawnParticle(Level world, @NotNull BlockPos pos, RandomSource random) {
		float randomScale = scaleVariance == 0 ? scale : (float) (scale + scaleVariance - random.nextDouble() * scaleVariance * 2.0D);
		int randomLifetime = lifetimeVariance == 0 ? lifetimeTicks : (int) (lifetimeTicks + lifetimeVariance - random.nextDouble() * lifetimeVariance * 2.0D);
		
		if (randomScale > 0 && randomLifetime > 0) {
			double randomOffsetX = sourcePositionVariance.x == 0 ? 0 : sourcePositionVariance.x - random.nextDouble() * sourcePositionVariance.x * 2.0D;
			double randomOffsetY = sourcePositionVariance.y == 0 ? 0 : sourcePositionVariance.y - random.nextDouble() * sourcePositionVariance.y * 2.0D;
			double randomOffsetZ = sourcePositionVariance.z == 0 ? 0 : sourcePositionVariance.z - random.nextDouble() * sourcePositionVariance.z * 2.0D;
			
			double randomVelocityX = velocityVariance.x == 0 ? 0 : velocityVariance.x - random.nextDouble() * velocityVariance.x * 2.0D;
			double randomVelocityY = velocityVariance.y == 0 ? 0 : velocityVariance.y - random.nextDouble() * velocityVariance.y * 2.0D;
			double randomVelocityZ = velocityVariance.z == 0 ? 0 : velocityVariance.z - random.nextDouble() * velocityVariance.z * 2.0D;
			
			var rgbColor = CMYtoRGB(cmyColor);
			
			world.addParticle(
					new DynamicParticleEffect(particleType, gravity, new Vector3f(rgbColor), randomScale, randomLifetime, collisions, glowing),
					(double) pos.getX() + 0.5 + sourcePosition.x + randomOffsetX,
					(double) pos.getY() + 0.5 + sourcePosition.y + randomOffsetY,
					(double) pos.getZ() + 0.5 + sourcePosition.z + randomOffsetZ,
					velocity.x + randomVelocityX,
					velocity.y + randomVelocityY,
					velocity.z + randomVelocityZ
			);
		}
	}
	
}
