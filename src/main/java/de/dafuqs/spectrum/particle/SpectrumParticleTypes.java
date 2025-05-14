package de.dafuqs.spectrum.particle;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.particle.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;

import java.util.function.*;

public class SpectrumParticleTypes {
	
	private static final DeferredRegistrar REGISTRAR = new DeferredRegistrar();
	
	public static final ParticleType<TransmissionParticleEffect> ITEM_TRANSMISSION = register("item_transmission", false, type -> TransmissionParticleEffect.CODEC, type -> TransmissionParticleEffect.PACKET_CODEC);
	public static final ParticleType<TransmissionParticleEffect> EXPERIENCE_TRANSMISSION = register("experience_transmission", false, type -> TransmissionParticleEffect.CODEC, type -> TransmissionParticleEffect.PACKET_CODEC);
	public static final ParticleType<TransmissionParticleEffect> WIRELESS_REDSTONE_TRANSMISSION = register("wireless_redstone_transmission", false, type -> TransmissionParticleEffect.CODEC, type -> TransmissionParticleEffect.PACKET_CODEC);
	public static final ParticleType<ColoredTransmissionParticleEffect> COLORED_TRANSMISSION = register("colored_transmission", false, type -> ColoredTransmissionParticleEffect.CODEC, type -> ColoredTransmissionParticleEffect.PACKET_CODEC);
	public static final ParticleType<TransmissionParticleEffect> BLOCK_POS_EVENT_TRANSMISSION = register("block_pos_event_transmission", false, type -> TransmissionParticleEffect.CODEC, type -> TransmissionParticleEffect.PACKET_CODEC);
	public static final ParticleType<PastelTransmissionParticleEffect> PASTEL_TRANSMISSION = register("pastel_transmission", false, type -> PastelTransmissionParticleEffect.CODEC, type -> PastelTransmissionParticleEffect.PACKET_CODEC);
	public static final ParticleType<TransmissionParticleEffect> HUMMINGSTONE_TRANSMISSION = register("hummingstone_transmission", false, type -> TransmissionParticleEffect.CODEC, type -> TransmissionParticleEffect.PACKET_CODEC);
	
	public static final SimpleParticleType SHIMMERSTONE_SPARKLE = register("shimmerstone_sparkle", false);
	public static final SimpleParticleType SHIMMERSTONE_SPARKLE_SMALL = register("shimmerstone_sparkle_small", false);
	public static final SimpleParticleType SHIMMERSTONE_SPARKLE_TINY = register("shimmerstone_sparkle_tiny", false);
	public static final SimpleParticleType VOID_FOG = register("void_fog", false);
	
	public static final SimpleParticleType RUNES = register("runes", false);
	public static final SimpleParticleType AZURE_DIKE_RUNES = register("azure_dike_runes", false);
	public static final SimpleParticleType AZURE_DIKE_RUNES_MAJOR = register("azure_dike_runes_major", false);
	public static final SimpleParticleType DRAKEBLOOD_DIKE_RUNES = register("drakeblood_dike_runes", false);
	public static final SimpleParticleType DRAKEBLOOD_DIKE_RUNES_MAJOR = register("drakeblood_dike_runes_major", false);
	public static final SimpleParticleType MALACHITE_DIKE_RUNES = register("malachite_dike_runes", false);
	public static final SimpleParticleType MALACHITE_DIKE_RUNES_MAJOR = register("malachite_dike_runes_major", false);
	
	public static final SimpleParticleType AZURE_AURA = register("azure_aura", false);
	public static final SimpleParticleType AZURE_MOTE = register("azure_mote", false);
	public static final SimpleParticleType AZURE_MOTE_SMALL = register("azure_mote_small", false);
	
	public static final SimpleParticleType BLUE_BUBBLE_POP = register("blue_bubble_pop", false);
	public static final SimpleParticleType GREEN_BUBBLE_POP = register("green_bubble_pop", false);
	
	public static final SimpleParticleType SPIRIT_SALLOW = register("spirit_sallow", false);
	public static final SimpleParticleType DIVINITY = register("divinity", false);
	public static final SimpleParticleType SHOOTING_STAR = register("shooting_star", false);
	public static final SimpleParticleType JADE_VINES = register("jade_vines", false);
	public static final SimpleParticleType JADE_VINES_BLOOM = register("jade_vines_bloom", false);
	public static final SimpleParticleType MOONSTONE_STRIKE = register("moonstone_strike", true);
	public static final SimpleParticleType MIRROR_IMAGE = register("mirror_image", true);
	
	public static final SimpleParticleType LAVA_FISHING = register("lava_fishing", false);
	
	public static final SimpleParticleType PRIMORDIAL_COSY_SMOKE = register("primordial_cosy_smoke", true);
	public static final SimpleParticleType PRIMORDIAL_SIGNAL_SMOKE = register("primordial_signal_smoke", true);
	public static final SimpleParticleType PRIMORDIAL_SMOKE = register("primordial_smoke", true);
	public static final SimpleParticleType PRIMORDIAL_FLAME = register("primordial_flame", true);
	public static final SimpleParticleType PRIMORDIAL_FLAME_SMALL = register("primordial_flame_small", true);
	
	public static final SimpleParticleType GOO_SPLASH = register("goo_splash", false);
	public static final SimpleParticleType DRIPPING_GOO = register("dripping_goo", false);
	public static final SimpleParticleType FALLING_GOO = register("falling_goo", false);
	public static final SimpleParticleType LANDING_GOO = register("landing_goo", false);
	public static final SimpleParticleType GOO_FISHING = register("goo_fishing", false);
	public static final SimpleParticleType GOO_POP = register("goo_pop", false);
	
	public static final SimpleParticleType LIQUID_CRYSTAL_SPLASH = register("liquid_crystal_splash", false);
	public static final SimpleParticleType DRIPPING_LIQUID_CRYSTAL = register("dripping_liquid_crystal", false);
	public static final SimpleParticleType FALLING_LIQUID_CRYSTAL = register("falling_liquid_crystal", false);
	public static final SimpleParticleType LANDING_LIQUID_CRYSTAL = register("landing_liquid_crystal", false);
	public static final SimpleParticleType LIQUID_CRYSTAL_FISHING = register("liquid_crystal_fishing", false);
	public static final SimpleParticleType LIQUID_CRYSTAL_SPARKLE = register("liquid_crystal_sparkle", false);
	
	public static final SimpleParticleType MIDNIGHT_SOLUTION_SPLASH = register("midnight_solution_splash", false);
	public static final SimpleParticleType DRIPPING_MIDNIGHT_SOLUTION = register("dripping_midnight_solution", false);
	public static final SimpleParticleType FALLING_MIDNIGHT_SOLUTION = register("falling_midnight_solution", false);
	public static final SimpleParticleType LANDING_MIDNIGHT_SOLUTION = register("landing_midnight_solution", false);
	public static final SimpleParticleType MIDNIGHT_SOLUTION_FISHING = register("midnight_solution_fishing", false);
	public static final SimpleParticleType DRAGONROT = register("dragonrot", false);
	
	public static final SimpleParticleType DRAGONROT_SPLASH = register("dragonrot_splash", false);
	public static final SimpleParticleType DRIPPING_DRAGONROT = register("dripping_dragonrot", false);
	public static final SimpleParticleType FALLING_DRAGONROT = register("falling_dragonrot", false);
	public static final SimpleParticleType LANDING_DRAGONROT = register("landing_dragonrot", false);
	public static final SimpleParticleType DRAGONROT_FISHING = register("dragonrot_fishing", false);
	
	public static final ParticleType<ColoredFallingSporeBlossomParticleEffect> COLORED_FALLING_SPORE_BLOSSOM = register("colored_falling_spore_blossom", false, (type) -> ColoredFallingSporeBlossomParticleEffect.CODEC, (type) -> ColoredFallingSporeBlossomParticleEffect.PACKET_CODEC);
	public static final ParticleType<ColoredSporeBlossomAirParticleEffect> COLORED_SPORE_BLOSSOM_AIR = register("colored_spore_blossom_air", false, (type) -> ColoredSporeBlossomAirParticleEffect.CODEC, (type) -> ColoredSporeBlossomAirParticleEffect.PACKET_CODEC);
	public static final ParticleType<ColoredCraftingParticleEffect> COLORED_CRAFTING = register("colored_crafting", false, (type) -> ColoredCraftingParticleEffect.CODEC, (type) -> ColoredCraftingParticleEffect.PACKET_CODEC);
	public static final ParticleType<ColoredFluidRisingParticleEffect> COLORED_FLUID_RISING = register("colored_fluid_rising", false, (type) -> ColoredFluidRisingParticleEffect.CODEC, (type) -> ColoredFluidRisingParticleEffect.PACKET_CODEC);
	public static final ParticleType<ColoredSparkleRisingParticleEffect> COLORED_SPARKLE_RISING = register("colored_sparkle_rising", false, (type) -> ColoredSparkleRisingParticleEffect.CODEC, (type) -> ColoredSparkleRisingParticleEffect.PACKET_CODEC);
	public static final ParticleType<ColoredExplosionParticleEffect> COLORED_EXPLOSION = register("colored_explosion", true, (type) -> ColoredExplosionParticleEffect.CODEC, (type) -> ColoredExplosionParticleEffect.PACKET_CODEC);
	
	// Biome particles
	public static final SimpleParticleType FALLING_ASH = register("falling_ash", true);
	public static final SimpleParticleType FIREFLY = register("firefly", true);
	public static final SimpleParticleType BLOODFLY = register("bloodfly", true);
	public static final SimpleParticleType QUARTZ_FLUFF = register("quartz_fluff", true);
	
	public static final SimpleParticleType LIGHT_RAIN = register("light_rain", true);
	public static final SimpleParticleType HEAVY_RAIN = register("heavy_rain", true);
	public static final SimpleParticleType RAIN_SPLASH = register("rain_splash", false);
	public static final SimpleParticleType RAIN_RIPPLE = register("rain_ripple", false);
	
	public static final SimpleParticleType LIGHT_TRAIL = register("light_trail", true);
	
	public static final ParticleType<DynamicParticleEffect> DYNAMIC = register("particle_spawner", false, type -> DynamicParticleEffect.CODEC, type -> DynamicParticleEffect.PACKET_CODEC);
	public static final ParticleType<DynamicParticleEffect> DYNAMIC_ALWAYS_SHOW = register("particle_spawner_always_show", true, type -> DynamicParticleEffect.CODEC, type -> DynamicParticleEffect.PACKET_CODEC);
	
	// Simple particles
	private static SimpleParticleType register(String name, boolean alwaysShow) {
		return REGISTRAR.defer(FabricParticleTypes.simple(alwaysShow), type -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, SpectrumCommon.locate(name), type));
	}
	
	// complex particles
	private static <T extends ParticleOptions> ParticleType<T> register(
			String name,
			boolean alwaysShow,
			Function<ParticleType<T>, MapCodec<T>> codecGetter,
			Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter
	) {
		return REGISTRAR.defer(new ParticleType<T>(alwaysShow) {
			@Override
			public MapCodec<T> codec() {
				return codecGetter.apply(this);
			}
			
			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return packetCodecGetter.apply(this);
			}
		}, type -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, SpectrumCommon.locate(name), type));
	}
	
	public static void register() {
		REGISTRAR.flush();
	}
	
}