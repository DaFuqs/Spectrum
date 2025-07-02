package de.dafuqs.spectrum.api.predicate.location;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.structure.*;

import java.util.*;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public record WorldConditionsPredicate(
		Optional<MoonPhasePredicate> moonPhase,
		Optional<TimeOfDayPredicate> timeOfDay,
		Optional<WeatherPredicate> weather,
		Optional<CommandPredicate> command,
		LocationPredicate location
) {
	
	public WorldConditionsPredicate(
			Optional<MoonPhasePredicate> moonPhase,
			Optional<TimeOfDayPredicate> timeOfDay,
			Optional<WeatherPredicate> weather,
			Optional<CommandPredicate> command,
			Optional<HolderSet<Biome>> biomes,
			Optional<HolderSet<Structure>> structures,
			Optional<ResourceKey<Level>> dimension,
			Optional<LightPredicate> light,
			Optional<BlockPredicate> block,
			Optional<FluidPredicate> fluid,
			Optional<Boolean> smokey,
			Optional<Boolean> canSeeSky
	) {
		this(moonPhase, timeOfDay, weather, command, new LocationPredicate(Optional.empty(), biomes, structures, dimension, smokey, light, block, fluid, canSeeSky));
	}
	
	public static final Codec<WorldConditionsPredicate> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			MoonPhasePredicate.CODEC.optionalFieldOf("moon_phase").forGetter(p -> p.moonPhase),
			TimeOfDayPredicate.CODEC.optionalFieldOf("time_of_day").forGetter(p -> p.timeOfDay),
			WeatherPredicate.CODEC.optionalFieldOf("weather").forGetter(p -> p.weather),
			CommandPredicate.CODEC.optionalFieldOf("command").forGetter(p -> p.command),
			RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(p -> p.location.biomes()),
			RegistryCodecs.homogeneousList(Registries.STRUCTURE).optionalFieldOf("structures").forGetter(p -> p.location.structures()),
			ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(p -> p.location.dimension()),
			LightPredicate.CODEC.optionalFieldOf("light").forGetter(p -> p.location.light()),
			BlockPredicate.CODEC.optionalFieldOf("block").forGetter(p -> p.location.block()),
			FluidPredicate.CODEC.optionalFieldOf("fluid").forGetter(p -> p.location.fluid()),
			Codec.BOOL.optionalFieldOf("smokey").forGetter(p -> p.location.smokey()),
			Codec.BOOL.optionalFieldOf("can_see_sky").forGetter(p -> p.location.canSeeSky())
	).apply(instance, WorldConditionsPredicate::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, WorldConditionsPredicate> PACKET_CODEC = PacketCodecHelper.tuple(
			ByteBufCodecs.optional(MoonPhasePredicate.PACKET_CODEC), p -> p.moonPhase,
			ByteBufCodecs.optional(TimeOfDayPredicate.PACKET_CODEC), p -> p.timeOfDay,
			ByteBufCodecs.optional(WeatherPredicate.PACKET_CODEC), p -> p.weather,
			ByteBufCodecs.optional(CommandPredicate.PACKET_CODEC), p -> p.command,
			ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.BIOME)), p -> p.location.biomes(),
			ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.STRUCTURE)), p -> p.location.structures(),
			ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)), p -> p.location.dimension(),
			ByteBufCodecs.optional(PacketCodecHelper.LIGHT_PREDICATE), p -> p.location.light(),
			ByteBufCodecs.optional(BlockPredicate.STREAM_CODEC), p -> p.location.block(),
			ByteBufCodecs.optional(PacketCodecHelper.FLUID_PREDICATE), p -> p.location.fluid(),
			ByteBufCodecs.optional(ByteBufCodecs.BOOL), p -> p.location.smokey(),
			ByteBufCodecs.optional(ByteBufCodecs.BOOL), p -> p.location.canSeeSky(),
			WorldConditionsPredicate::new
	);
	
	public boolean test(ServerLevel world, BlockPos pos) {
		return location.matches(world, pos.getX(), pos.getY(), pos.getZ())
				&& moonPhase.map(p -> p.test(world)).orElse(true)
				&& timeOfDay.map(p -> p.test(world)).orElse(true)
				&& weather.map(p -> p.test(world)).orElse(true)
				&& command.map(p -> p.test(world, pos)).orElse(true);
	}
}
