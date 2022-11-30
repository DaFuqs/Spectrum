package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class SpectrumBlockLeakParticles {
	
	
	@Environment(EnvType.CLIENT)
	public static class LandingMudFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public LandingMudFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.Landing(clientWorld, d, e, f, SpectrumFluids.MUD);
			blockLeakParticle.setColor(SpectrumFluids.MUD_COLOR.getX(), SpectrumFluids.MUD_COLOR.getY(), SpectrumFluids.MUD_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	public static class FallingMudFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public FallingMudFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.ContinuousFalling(clientWorld, d, e, f, SpectrumFluids.MUD, SpectrumParticleTypes.LANDING_MUD);
			blockLeakParticle.setColor(SpectrumFluids.MUD_COLOR.getX(), SpectrumFluids.MUD_COLOR.getY(), SpectrumFluids.MUD_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	public static class DrippingMudFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public DrippingMudFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.Dripping(clientWorld, d, e, f, SpectrumFluids.MUD, SpectrumParticleTypes.FALLING_MUD);
			blockLeakParticle.setColor(SpectrumFluids.MUD_COLOR.getX(), SpectrumFluids.MUD_COLOR.getY(), SpectrumFluids.MUD_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static class LandingLiquidCrystalFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public LandingLiquidCrystalFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.Landing(clientWorld, d, e, f, SpectrumFluids.LIQUID_CRYSTAL);
			blockLeakParticle.setColor(SpectrumFluids.LIQUID_CRYSTAL_COLOR.getX(), SpectrumFluids.LIQUID_CRYSTAL_COLOR.getY(), SpectrumFluids.LIQUID_CRYSTAL_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	public static class FallingLiquidCrystalFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public FallingLiquidCrystalFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.ContinuousFalling(clientWorld, d, e, f, SpectrumFluids.LIQUID_CRYSTAL, SpectrumParticleTypes.LANDING_LIQUID_CRYSTAL);
			blockLeakParticle.setColor(SpectrumFluids.LIQUID_CRYSTAL_COLOR.getX(), SpectrumFluids.LIQUID_CRYSTAL_COLOR.getY(), SpectrumFluids.LIQUID_CRYSTAL_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	public static class DrippingLiquidCrystalFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public DrippingLiquidCrystalFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.Dripping(clientWorld, d, e, f, SpectrumFluids.LIQUID_CRYSTAL, SpectrumParticleTypes.FALLING_LIQUID_CRYSTAL);
			blockLeakParticle.setColor(SpectrumFluids.LIQUID_CRYSTAL_COLOR.getX(), SpectrumFluids.LIQUID_CRYSTAL_COLOR.getY(), SpectrumFluids.LIQUID_CRYSTAL_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static class LandingMidnightSolutionFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public LandingMidnightSolutionFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.Landing(clientWorld, d, e, f, SpectrumFluids.MIDNIGHT_SOLUTION);
			blockLeakParticle.setColor(SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getX(), SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getY(), SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	public static class FallingMidnightSolutionFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public FallingMidnightSolutionFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.ContinuousFalling(clientWorld, d, e, f, SpectrumFluids.MIDNIGHT_SOLUTION, SpectrumParticleTypes.LANDING_MIDNIGHT_SOLUTION);
			blockLeakParticle.setColor(SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getX(), SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getY(), SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
	public static class DrippingMidnightSolutionFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;
		
		public DrippingMidnightSolutionFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.Dripping(clientWorld, d, e, f, SpectrumFluids.MIDNIGHT_SOLUTION, SpectrumParticleTypes.FALLING_MIDNIGHT_SOLUTION);
			blockLeakParticle.setColor(SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getX(), SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getY(), SpectrumFluids.MIDNIGHT_SOLUTION_COLOR.getZ());
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}
	
}
