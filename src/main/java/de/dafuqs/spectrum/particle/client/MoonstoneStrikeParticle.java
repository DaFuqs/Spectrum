package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class MoonstoneStrikeParticle extends NoRenderParticle {
	
	private final static int MAX_AGE = 40;
	
	MoonstoneStrikeParticle(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
	}
	
	@Override
	public void tick() {
		if (this.age == 0) {
			world.addParticle(SpectrumParticleTypes.WHITE_EXPLOSION, this.x, this.y, this.z, 0, 0, 0);
		}
		var alt = random.nextBoolean();
		var particle = alt ? SpectrumParticleTypes.SHOOTING_STAR : SpectrumParticleTypes.WHITE_SPARKLE_RISING;
		var velocity = alt ? 0.5F : 0.375F;
		var rotation = Math.PI / 20F * age;
		var nextRotation = Math.PI / 20F * (age + 1);
		ParticleHelper.playParticleWithRotation(this.world, new Vec3d(this.x, this.y, this.z), rotation, rotation, particle, VectorPattern.EIGHT, velocity);
		ParticleHelper.playParticleWithRotation(this.world, new Vec3d(this.x, this.y, this.z), nextRotation, -nextRotation, particle, VectorPattern.EIGHT, velocity);

		this.age += 2;
		if (this.age == MAX_AGE) {
			this.markDead();
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		public Factory() {
		}
		
		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new MoonstoneStrikeParticle(clientWorld, d, e, f);
		}
	}

}
