package de.dafuqs.spectrum.particle.client;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import org.joml.*;

import java.lang.Math;

public class RaindropParticle extends TextureSheetParticle {
	
	private static final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
	
	public RaindropParticle(ClientLevel clientWorld, double d, double e, double f, SpriteSet spriteProvider) {
		super(clientWorld, d, e, f);
		pickSprite(spriteProvider);
		gravity = 5.25F;
		quadSize = 0.1F + random.nextFloat() * 0.3125F;
		lifetime = 25;
		pos.set(x, y, z);
		var waterColor = SpectrumColorHelper.colorIntToVec(BiomeColors.getAverageWaterColor(level, pos));
		rCol = waterColor.x;
		gCol = waterColor.y;
		bCol = waterColor.z;
	}
	
	@Override
	public void tick() {
		pos.set(x, y, z);
		var waterColor = SpectrumColorHelper.colorIntToVec(BiomeColors.getAverageWaterColor(level, pos));
		rCol = waterColor.x;
		gCol = waterColor.y;
		bCol = waterColor.z;
		
		if (onGround) {
			spawnDroplets(0.85F, 4, false);
			remove();
			return;
		} else if (!level.getFluidState(pos).isEmpty()) {
			spawnDroplets(0.625F, 7, true);
			remove();
			return;
		}
		
		adjustAlpha();
		super.tick();
	}
	
	private void spawnDroplets(float velMult, int drops, boolean water) {
		var state = level.getBlockState(pos);
		var spawnY = y + 0.01F;
		
		if (water) {
			spawnY = Math.ceil(y) - 0.05F;
		} else if (state.is(SpectrumBlocks.ROTTEN_GROUND)) {
			spawnY = pos.getY() + 1.01F;
		}
		
		if (isAlive()) {
			var spawns = random.nextInt(drops) + 1;
			for (int i = 0; i < spawns; i++) {
				var xVel = random.nextFloat() * 0.8 - 0.4F;
				var zVel = random.nextFloat() * 0.8 - 0.4F;
				level.addParticle(SpectrumParticleTypes.RAIN_SPLASH, x, spawnY, z, xVel * velMult, 0, zVel * velMult);
			}
			level.addParticle(SpectrumParticleTypes.RAIN_RIPPLE, x, spawnY, z, 0, 0, 0);
		}
	}
	
	private void adjustAlpha() {
		if (age <= 5) {
			alpha = Mth.clamp(age / 5F, 0, 1F);
			return;
		}
		
		var ageFade = Mth.clamp(Math.min(lifetime - age, 5) / 5F, 0, 1F);
		alpha = Math.min(alpha, ageFade);
		
		if (alpha < 0.01F) {
			remove();
		}
	}
	
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3 vec3d = camera.getPosition();
		float f = (float) (Mth.lerp(tickDelta, this.xo, this.x) - vec3d.x());
		float g = (float) (Mth.lerp(tickDelta, this.yo, this.y) - vec3d.y());
		float h = (float) (Mth.lerp(tickDelta, this.zo, this.z) - vec3d.z());
		var xOffset = x - camera.getPosition().x;
		var zOffset = z - camera.getPosition().z;
		
		Quaternionf quaternionf = Axis.YP.rotation((float) Mth.atan2(xOffset, zOffset));
		
		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-0.75F, -1.75F, 0.0F), new Vector3f(-0.75F, 1.75F, 0.0F), new Vector3f(0.75F, 1.75F, 0.0F), new Vector3f(0.75F, -1.75F, 0.0F)};
		float i = this.getQuadSize(tickDelta);
		
		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
			vector3f.rotate(quaternionf);
			vector3f.mul(i);
			vector3f.add(f, g, h);
		}
		
		float k = this.getU0();
		float l = this.getU1();
		float m = this.getV0();
		float n = this.getV1();
		int o = this.getLightColor(tickDelta);
		vertexConsumer.addVertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).setUv(l, n).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(o);
		vertexConsumer.addVertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).setUv(l, m).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(o);
		vertexConsumer.addVertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).setUv(k, m).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(o);
		vertexConsumer.addVertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).setUv(k, n).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(o);
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new RaindropParticle(clientWorld, d, e, f, spriteProvider);
		}
	}
}
