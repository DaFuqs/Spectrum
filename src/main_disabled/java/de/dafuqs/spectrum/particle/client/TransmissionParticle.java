package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TransmissionParticle extends TextureSheetParticle {
	
	private final PositionSource positionSource;
	
	public TransmissionParticle(ClientLevel world, double x, double y, double z, PositionSource positionSource, int arrivalInTicks) {
		super(world, x, y, z);
		this.quadSize = 0.3F;
		this.alpha = 0.7F;
		
		this.lifetime = arrivalInTicks;
		this.positionSource = positionSource;
	}
	
	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			Optional<Vec3> optional = this.positionSource.getPosition(this.level);
			if (optional.isEmpty()) {
				this.remove();
			} else {
				int i = this.lifetime - this.age;
				double d = 1.0 / (double) i;
				Vec3 vec3d = optional.get();
				this.x = Mth.lerp(d, this.x, vec3d.x());
				this.y = Mth.lerp(d, this.y, vec3d.y());
				this.z = Mth.lerp(d, this.z, vec3d.z());
			}
		}
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<TransmissionParticleEffect> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(TransmissionParticleEffect particleEffect, ClientLevel world, double x, double y, double z, double g, double h, double i) {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, particleEffect.getDestination(), particleEffect.getArrivalInTicks());
			particle.pickSprite(this.spriteProvider);
			return particle;
		}
	}
	
}
