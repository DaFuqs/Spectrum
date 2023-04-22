package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ParticleSpawnerConfiguration {
	
	private final ParticleType<?> particleType;
	private final Vec3i cmyColor; // 0-100 cmy
	private final boolean glowing;
	private final float particlesPerSecond; /* >1 = every xth tick */
	private final Vec3f sourcePosition;
	private final Vec3f sourcePositionVariance;
	private final Vec3f velocity;
	private final Vec3f velocityVariance;
	private final float scale;
	private final float scaleVariance;
	private final int lifetimeTicks;
	private final int lifetimeVariance;
	private final float gravity;
	private final boolean collisions;
	
	private final Vec3f rgbColor; // 0-255 rgb
	
	public ParticleSpawnerConfiguration(ParticleType<?> particleType, Vec3i cmyColor, boolean glowing, float particlesPerSecond /* >1 = every xth tick */,
										Vec3f sourcePosition, Vec3f sourcePositionVariance, Vec3f velocity, Vec3f velocityVariance,
										float scale, float scaleVariance, int lifetimeTicks, int lifetimeVariance, float gravity, boolean collisions) {
		
		this.particleType = particleType;
		this.cmyColor = cmyColor;
		this.glowing = glowing;
		this.particlesPerSecond = particlesPerSecond;
		this.sourcePosition = sourcePosition;
		this.sourcePositionVariance = sourcePositionVariance;
		this.velocity = velocity;
		this.velocityVariance = velocityVariance;
		this.scale = scale;
		this.scaleVariance = scaleVariance;
		this.lifetimeTicks = lifetimeTicks;
		this.lifetimeVariance = lifetimeVariance;
		this.gravity = gravity;
		this.collisions = collisions;
		
		this.rgbColor = CMYtoRGB(cmyColor);
	}
	
	public static Vec3f CMYtoRGB(Vec3i cmy) {
		float r = 1F - cmy.getX() / 100F;
		float g = 1F - cmy.getY() / 100F;
		float b = 1F - cmy.getZ() / 100F;
		return new Vec3f(r, g, b);
	}
	
	public ParticleType<?> getParticleType() {
		return particleType;
	}
	
	public Vec3i getCmyColor() {
		return cmyColor;
	}
	
	public boolean glows() {
		return glowing;
	}
	
	public float getParticlesPerSecond() {
		return particlesPerSecond;
	}
	
	public Vec3f getSourcePosition() {
		return sourcePosition;
	}
	
	public Vec3f getSourcePositionVariance() {
		return sourcePositionVariance;
	}
	
	public Vec3f getVelocity() {
		return velocity;
	}
	
	public Vec3f getVelocityVariance() {
		return velocityVariance;
	}
	
	public float getScale() {
		return scale;
	}
	
	public float getScaleVariance() {
		return scaleVariance;
	}
	
	public int getLifetimeTicks() {
		return lifetimeTicks;
	}
	
	public int getLifetimeVariance() {
		return lifetimeVariance;
	}
	
	public float getGravity() {
		return gravity;
	}
	
	public boolean hasCollisions() {
		return collisions;
	}
	
	public void write(PacketByteBuf buf) {
		buf.writeString(Registry.PARTICLE_TYPE.getId(particleType).toString());
		buf.writeInt(cmyColor.getX());
		buf.writeInt(cmyColor.getY());
		buf.writeInt(cmyColor.getZ());
		buf.writeBoolean(glowing);
		buf.writeFloat(particlesPerSecond);
		buf.writeFloat(sourcePosition.getX());
		buf.writeFloat(sourcePosition.getY());
		buf.writeFloat(sourcePosition.getZ());
		buf.writeFloat(sourcePositionVariance.getX());
		buf.writeFloat(sourcePositionVariance.getY());
		buf.writeFloat(sourcePositionVariance.getZ());
		buf.writeFloat(velocity.getX());
		buf.writeFloat(velocity.getY());
		buf.writeFloat(velocity.getZ());
		buf.writeFloat(velocityVariance.getX());
		buf.writeFloat(velocityVariance.getY());
		buf.writeFloat(velocityVariance.getZ());
		buf.writeFloat(scale);
		buf.writeFloat(scaleVariance);
		buf.writeInt(lifetimeTicks);
		buf.writeInt(lifetimeVariance);
		buf.writeFloat(gravity);
		buf.writeBoolean(collisions);
	}
	
	public static ParticleSpawnerConfiguration fromBuf(PacketByteBuf buf) {
		Identifier particleIdentifier = new Identifier(buf.readString());
		ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(particleIdentifier);
		Vec3i cmyColor = new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
		boolean glowing = buf.readBoolean();
		float particlesPerSecond = buf.readFloat();
		Vec3f sourcePosition = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
		Vec3f sourcePositionVariance = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
		Vec3f velocity = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
		Vec3f velocityVariance = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
		float scale = buf.readFloat();
		float scaleVariance = buf.readFloat();
		int lifetimeTicks = buf.readInt();
		int lifetimeVariance = buf.readInt();
		float gravity = buf.readFloat();
		boolean collisions = buf.readBoolean();
		
		return new ParticleSpawnerConfiguration(particleType, cmyColor, glowing, particlesPerSecond, sourcePosition, sourcePositionVariance,
				velocity, velocityVariance, scale, scaleVariance, lifetimeTicks, lifetimeVariance, gravity, collisions);
	}
	
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("particle_type_identifier", Registry.PARTICLE_TYPE.getId(particleType).toString());
		nbt.putFloat("particles_per_tick", particlesPerSecond);
		nbt.putBoolean("glowing", glowing);
		nbt.putFloat("source_pos_x", sourcePosition.getX());
		nbt.putFloat("source_pos_y", sourcePosition.getY());
		nbt.putFloat("source_pos_z", sourcePosition.getZ());
		nbt.putFloat("source_pos_variance_x", sourcePositionVariance.getX());
		nbt.putFloat("source_pos_variance_y", sourcePositionVariance.getY());
		nbt.putFloat("source_pos_variance_z", sourcePositionVariance.getZ());
		nbt.putFloat("source_velocity_x", velocity.getX());
		nbt.putFloat("source_velocity_y", velocity.getY());
		nbt.putFloat("source_velocity_z", velocity.getZ());
		nbt.putFloat("source_velocity_variance_x", velocityVariance.getX());
		nbt.putFloat("source_velocity_variance_y", velocityVariance.getY());
		nbt.putFloat("source_velocity_variance_z", velocityVariance.getZ());
		nbt.putInt("color_c", cmyColor.getX());
		nbt.putInt("color_m", cmyColor.getY());
		nbt.putInt("color_y", cmyColor.getZ());
		nbt.putFloat("scale", scale);
		nbt.putFloat("scale_variance", scaleVariance);
		nbt.putInt("lifetime", lifetimeTicks);
		nbt.putInt("lifetime_variance", lifetimeVariance);
		nbt.putFloat("gravity", gravity);
		nbt.putBoolean("collisions", collisions);
		return nbt;
	}
	
	public static ParticleSpawnerConfiguration fromNbt(NbtCompound tag) {
		ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(new Identifier(tag.getString("particle_type_identifier")));
		float particlesPerSecond = tag.getFloat("particles_per_tick");
		boolean glowing = tag.getBoolean("glowing");
		Vec3f particleSourcePosition = new Vec3f(tag.getFloat("source_pos_x"), tag.getFloat("source_pos_y"), tag.getFloat("source_pos_z"));
		Vec3f particleSourcePositionVariance = new Vec3f(tag.getFloat("source_pos_variance_x"), tag.getFloat("source_pos_variance_y"), tag.getFloat("source_pos_variance_z"));
		Vec3f velocity = new Vec3f(tag.getFloat("source_velocity_x"), tag.getFloat("source_velocity_y"), tag.getFloat("source_velocity_z"));
		Vec3f velocityVariance = new Vec3f(tag.getFloat("source_velocity_variance_x"), tag.getFloat("source_velocity_variance_y"), tag.getFloat("source_velocity_variance_z"));
		Vec3i cmyColor = new Vec3i(tag.getInt("color_c"), tag.getInt("color_m"), tag.getInt("color_y"));
		float scale = tag.getFloat("scale");
		float scaleVariance = tag.getFloat("scale_variance");
		int lifetimeTicks = tag.getInt("lifetime");
		int lifetimeVariance = tag.getInt("lifetime_variance");
		float gravity = tag.getFloat("gravity");
		boolean collisions = tag.getBoolean("collisions");
		
		return new ParticleSpawnerConfiguration(particleType, cmyColor, glowing, particlesPerSecond, particleSourcePosition, particleSourcePositionVariance,
				velocity, velocityVariance, scale, scaleVariance, lifetimeTicks, lifetimeVariance, gravity, collisions);
	}
	
	public void spawnParticles(World world, @NotNull BlockPos pos) {
		float particlesToSpawn = particlesPerSecond / 20F;
		while (particlesToSpawn >= 1 || world.random.nextFloat() < particlesToSpawn) {
			spawnParticle(world, pos, world.random);
			particlesToSpawn--;
		}
	}
	
	private void spawnParticle(World world, @NotNull BlockPos pos, Random random) {
		float randomScale = scaleVariance == 0 ? scale : (float) (scale + scaleVariance - random.nextDouble() * scaleVariance * 2.0D);
		int randomLifetime = lifetimeVariance == 0 ? lifetimeTicks : (int) (lifetimeTicks + lifetimeVariance - random.nextDouble() * lifetimeVariance * 2.0D);
		
		if (randomScale > 0 && randomLifetime > 0) {
			double randomOffsetX = sourcePositionVariance.getX() == 0 ? 0 : sourcePositionVariance.getX() - random.nextDouble() * sourcePositionVariance.getX() * 2.0D;
			double randomOffsetY = sourcePositionVariance.getY() == 0 ? 0 : sourcePositionVariance.getY() - random.nextDouble() * sourcePositionVariance.getY() * 2.0D;
			double randomOffsetZ = sourcePositionVariance.getZ() == 0 ? 0 : sourcePositionVariance.getZ() - random.nextDouble() * sourcePositionVariance.getZ() * 2.0D;
			
			double randomVelocityX = velocityVariance.getX() == 0 ? 0 : velocityVariance.getX() - random.nextDouble() * velocityVariance.getX() * 2.0D;
			double randomVelocityY = velocityVariance.getY() == 0 ? 0 : velocityVariance.getY() - random.nextDouble() * velocityVariance.getY() * 2.0D;
			double randomVelocityZ = velocityVariance.getZ() == 0 ? 0 : velocityVariance.getZ() - random.nextDouble() * velocityVariance.getZ() * 2.0D;
			
			world.addParticle(
					new DynamicParticleEffect(particleType, gravity, rgbColor, randomScale, randomLifetime, collisions, glowing),
					(double) pos.getX() + 0.5 + sourcePosition.getX() + randomOffsetX,
					(double) pos.getY() + 0.5 + sourcePosition.getY() + randomOffsetY,
					(double) pos.getZ() + 0.5 + sourcePosition.getZ() + randomOffsetZ,
					velocity.getX() + randomVelocityX,
					velocity.getY() + randomVelocityY,
					velocity.getZ() + randomVelocityZ
			);
		}
	}
	
}
