package de.dafuqs.spectrum.particle;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.particle.client.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.particle.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.minecraft.world.level.material.*;

// See ParticleManager for vanilla
@Environment(EnvType.CLIENT)
public class SpectrumParticleFactories {
	
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_CRAFTING, ColoredCraftingParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_FLUID_RISING, FixedVelocityParticle.ColoredFluidRisingFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_SPARKLE_RISING, FixedVelocityParticle.ColoredSparkleRisingFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_EXPLOSION, ColoredExplosionParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_FALLING_SPORE_BLOSSOM, ColoredBlockLeakParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_SPORE_BLOSSOM_AIR, ColoredWaterSuspendParticle.Factory::new);
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.ITEM_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
			particle.pickSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.EXPERIENCE_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
			particle.pickSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
			particle.pickSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ColoredTransmissionParticle particle = new ColoredTransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks(), parameters.getColor());
			particle.pickSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLOCK_POS_EVENT_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
			particle.pickSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PASTEL_TRANSMISSION, provider -> (pastelTransmissionParticleEffect, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			PastelTransmissionParticle particle = new PastelTransmissionParticle(Minecraft.getInstance().getItemRenderer(), world, x, y, z, pastelTransmissionParticleEffect.nodePositions(), pastelTransmissionParticleEffect.stack(), pastelTransmissionParticleEffect.travelTime(), pastelTransmissionParticleEffect.color());
			particle.pickSprite(provider);
			float[] color = PastelRenderHelper.unpackNormalizedColor(pastelTransmissionParticleEffect.color());
			particle.setColor(color[1], color[2], color[3]);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.HUMMINGSTONE_TRANSMISSION, TransmissionParticle.Factory::new);
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MOONSTONE_STRIKE, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			MoonstoneStrikeParticle.Factory factory = new MoonstoneStrikeParticle.Factory();
			return factory.createParticle(SpectrumParticleTypes.MOONSTONE_STRIKE, world, x, y, z, velocityX, velocityY, velocityZ);
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PRIMORDIAL_COSY_SMOKE, LargePrimordialSmokeParticle.CosySmokeFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PRIMORDIAL_SIGNAL_SMOKE, LargePrimordialSmokeParticle.SignalSmokeFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PRIMORDIAL_SMOKE, PrimordialSmokeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PRIMORDIAL_FLAME, FlameParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PRIMORDIAL_FLAME_SMALL, FlameParticle.SmallFlameProvider::new);
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DIVINITY, HardcoreParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHOOTING_STAR, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_SMALL, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_TINY, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRAGONROT, BubblePopParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.VOID_FOG, VoidFogParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.GOO_POP, BubblePopParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLUE_BUBBLE_POP, BubblePopParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.GREEN_BUBBLE_POP, BubblePopParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPIRIT_SALLOW, WindParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.JADE_VINES, ZigZagParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.JADE_VINES_BLOOM, ZigZagParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MIRROR_IMAGE, LitParticle.Factory::new);
		
		// Runes / Dike
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.RUNES, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.AZURE_DIKE_RUNES, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.AZURE_DIKE_RUNES_MAJOR, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRAKEBLOOD_DIKE_RUNES, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRAKEBLOOD_DIKE_RUNES_MAJOR, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MALACHITE_DIKE_RUNES, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MALACHITE_DIKE_RUNES_MAJOR, LitParticle.Factory::new);
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.AZURE_AURA, AzureAuraParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.AZURE_MOTE, AzureMoteParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.AZURE_MOTE_SMALL, AzureMoteParticle.Factory::new);
		
		// Fluid Splash
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.GOO_SPLASH, SplashParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_SPLASH, SplashParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH, SplashParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRAGONROT_SPLASH, SplashParticle.Provider::new);
		
		// Fluid Dripping
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRIPPING_GOO, SpectrumBlockLeakParticles.DrippingGooFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRIPPING_LIQUID_CRYSTAL, SpectrumBlockLeakParticles.DrippingLiquidCrystalFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRIPPING_MIDNIGHT_SOLUTION, SpectrumBlockLeakParticles.DrippingMidnightSolutionFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRIPPING_DRAGONROT, SpectrumBlockLeakParticles.DrippingDragonrotFactory::new);
		
		// Fluid Falling
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_GOO, SpectrumBlockLeakParticles.FallingGooFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_LIQUID_CRYSTAL, SpectrumBlockLeakParticles.FallingLiquidCrystalFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_MIDNIGHT_SOLUTION, SpectrumBlockLeakParticles.FallingMidnightSolutionFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_DRAGONROT, SpectrumBlockLeakParticles.FallingDragonrotFactory::new);
		
		// Fluid Landing
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LANDING_GOO, SpectrumBlockLeakParticles.LandingGooFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LANDING_LIQUID_CRYSTAL, SpectrumBlockLeakParticles.LandingLiquidCrystalFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LANDING_MIDNIGHT_SOLUTION, SpectrumBlockLeakParticles.LandingMidnightSolutionFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LANDING_DRAGONROT, SpectrumBlockLeakParticles.LandingDragonrotFactory::new);
		
		// Fluid Fishing
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LAVA_FISHING, WakeParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.GOO_FISHING, WakeParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING, WakeParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MIDNIGHT_SOLUTION_FISHING, WakeParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRAGONROT_FISHING, WakeParticle.Provider::new);
		
		//Azzyy sucked cock here
		//Can confirm this sucks ~Daf
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIGHT_TRAIL, LightTrailParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_ASH, FallingAshParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FIREFLY, FireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLOODFLY, BloodflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.QUARTZ_FLUFF, QuartzFluffParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIGHT_RAIN, RaindropParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.HEAVY_RAIN, RaindropParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.RAIN_SPLASH, TranslucentSplashParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.RAIN_RIPPLE, RainRippleParticle.Factory::new);
		
		// Since these can reference other particle types, they should always come last
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DYNAMIC, DynamicParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DYNAMIC_ALWAYS_SHOW, DynamicParticle.Factory::new);
	}
	
	public static TextureSheetParticle createFallingSporeBlossom(SimpleParticleType type, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		int i = (int) (64.0F / Mth.randomBetween(world.getRandom(), 0.1F, 0.9F));
		DripParticle blockLeakParticle = new DripParticle.FallingParticle(world, x, y, z, Fluids.EMPTY, i);
		blockLeakParticle.gravity = 0.005F;
		(blockLeakParticle).setColor(0.32F, 0.5F, 0.22F);
		return blockLeakParticle;
	}
	
	
}
