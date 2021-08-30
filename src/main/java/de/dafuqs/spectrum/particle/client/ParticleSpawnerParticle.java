package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.ParticleSpawnerParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ParticleSpawnerParticle extends SpriteBillboardParticle {

	public ParticleSpawnerParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		super(clientWorld, d, e, f, g, h, i);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public void apply(@NotNull ParticleSpawnerParticleEffect effect) {
		Sprite sprite = ((ParticleManagerAccessor) MinecraftClient.getInstance().particleManager).getParticleAtlasTexture().getSprite(effect.textureIdentifier);
		this.setSprite(sprite);
		this.setMaxAge(effect.lifetimeTicks);
		this.scale(effect.scale);
		this.setColor(effect.color.getX(), effect.color.getY(), effect.color.getZ()); // TODO: change to CMY
		this.field_28786 = effect.gravity;
		this.collidesWithWorld = effect.collisions;
	}

}