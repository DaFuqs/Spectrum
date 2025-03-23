package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.color.world.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import org.joml.*;

import java.lang.Math;

public class RaindropParticle extends SpriteBillboardParticle {
	
	private static final Vec3d VERTICAL = new Vec3d(0, 1, 0);
	private static final BlockPos.Mutable pos = new BlockPos.Mutable();
	//private final int simInterval = SpectrumCommon.CONFIG.WindSimInterval, simOffset;
	
	public RaindropParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider) {
		super(clientWorld, d, e, f);
		setSprite(spriteProvider);
		gravityStrength = 5.25F;
		scale = 0.1F + random.nextFloat() * 0.3125F;
		//this.simOffset = random.nextInt(simInterval);
		maxAge = 25;
		pos.set(x, y, z);
		var waterColor = ColorHelper.colorIntToVec(BiomeColors.getWaterColor(world, pos));
		red = waterColor.x;
		green = waterColor.y;
		blue = waterColor.z;
	}

	@Override
	public void tick() {
		pos.set(x, y, z);
		var waterColor = ColorHelper.colorIntToVec(BiomeColors.getWaterColor(world, pos));
		red = waterColor.x;
		green = waterColor.y;
		blue = waterColor.z;

		if (onGround) {
			spawnDroplets(0.85F, 4, false);
			markDead();
			return;
		}
		else if(!world.getFluidState(pos).isEmpty()) {
			spawnDroplets(0.625F, 7, true);
			markDead();
			return;
		}
		
		adjustAlpha();
		super.tick();
	}

	private void spawnDroplets(float velMult, int drops, boolean water) {
		var state = world.getBlockState(pos);
		var spawnY = y + 0.01F;

		if (water) {
			spawnY = Math.ceil(y) - 0.05F;
		}
		else if(state.isOf(SpectrumBlocks.ROTTEN_GROUND)){
			spawnY = pos.getY() + 1.01F;
		}

		if (isAlive()) {
			var spawns = random.nextInt(drops) + 1;
			for (int i = 0; i < spawns; i++) {
				var xVel = random.nextFloat() * 0.8 - 0.4F;
				var zVel = random.nextFloat() * 0.8 - 0.4F;
				world.addParticle(SpectrumParticleTypes.RAIN_SPLASH, x, spawnY, z, xVel * velMult, 0, zVel * velMult);
			}
			world.addParticle(SpectrumParticleTypes.RAIN_RIPPLE, x, spawnY, z, 0, 0, 0);
		}
	}

	private void adjustAlpha() {
		if (age <= 5) {
			alpha = MathHelper.clamp(age / 5F, 0, 1F);
			return;
		}
		
		var ageFade = MathHelper.clamp(Math.min(maxAge - age, 5) / 5F, 0, 1F);
		alpha = Math.min(alpha, ageFade);
		
		if (alpha < 0.01F) {
			markDead();
		}
	}
	
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		var xOffset = x - camera.getPos().x;
		var zOffset = z - camera.getPos().z;
		
		Quaternionf quaternionf = RotationAxis.POSITIVE_Y.rotation((float) MathHelper.atan2(xOffset, zOffset));
		
		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-0.75F, -1.75F, 0.0F), new Vector3f(-0.75F, 1.75F, 0.0F), new Vector3f(0.75F, 1.75F, 0.0F), new Vector3f(0.75F, -1.75F, 0.0F)};
		float i = this.getSize(tickDelta);
		
		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
			vector3f.rotate(quaternionf);
			vector3f.mul(i);
			vector3f.add(f, g, h);
		}
		
		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vector3fs[0].x(), (double) vector3fs[0].y(), (double) vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), (double) vector3fs[1].y(), (double) vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), (double) vector3fs[2].y(), (double) vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[3].x(), (double) vector3fs[3].y(), (double) vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new RaindropParticle(clientWorld, d, e, f, spriteProvider);
		}
	}
}
