package de.dafuqs.spectrum.particle.client;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.helpers.*;
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

public class RainRippleParticle extends TextureSheetParticle {
	
	private float width, lastWidth, alphaMult;
	
	public RainRippleParticle(ClientLevel clientWorld, double d, double e, double f, SpriteSet spriteProvider) {
		super(clientWorld, d, e, f);
		pickSprite(spriteProvider);
		gravity = 0F;
		quadSize = 0.2F + random.nextFloat() * 0.1F;
		alphaMult = 0.75F + random.nextFloat() * 0.25F;
		alpha = alphaMult;
		var waterColor = SpectrumColorHelper.colorIntToVec(BiomeColors.getAverageWaterColor(level, BlockPos.containing(x, y, z)));
		rCol = waterColor.x;
		gCol = waterColor.y;
		bCol = waterColor.z;
		lifetime = 13;
	}
	
	@Override
	public void tick() {
		lastWidth = width;
		width = Math.max(0.05F, age / 13F) + 0.1F;
		adjustAlpha();
		super.tick();
	}
	
	private void adjustAlpha() {
		alpha = Mth.clamp(Math.min(lifetime - age, 6) / 6F, 0, alphaMult);
		if (alpha < 0.01F) {
			remove();
		}
	}
	
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3 vec3d = camera.getPosition();
		float f = (float) (Mth.lerp(tickDelta, this.xo, this.x) - vec3d.x());
		float g = (float) (Mth.lerp(tickDelta, this.yo, this.y) - vec3d.y());
		float h = (float) (Mth.lerp(tickDelta, this.zo, this.z) - vec3d.z());
		
		var effWidth = Mth.lerp(tickDelta, lastWidth, width);
		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-effWidth, 0, -effWidth), new Vector3f(-effWidth, 0, effWidth), new Vector3f(effWidth, 0, effWidth), new Vector3f(effWidth, 0, -effWidth)};
		float i = this.getQuadSize(tickDelta);
		
		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
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
			return new RainRippleParticle(clientWorld, d, e, f, spriteProvider);
		}
	}
}
