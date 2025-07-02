package de.dafuqs.spectrum.recipe.titration_barrel;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import io.netty.buffer.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.effect.*;

import java.util.*;

public record FermentationStatusEffectEntry(
		MobEffect statusEffect,
		int baseDuration,
		List<StatusEffectPotencyEntry> potencyEntries
) {
	
	public static final Codec<FermentationStatusEffectEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.MOB_EFFECT.byNameCodec().orElse(err -> {
				SpectrumCommon.logError(err + ". Falling back to WEAKNESS");
				return err;
			}, MobEffects.WEAKNESS.value()).fieldOf("id").forGetter(FermentationStatusEffectEntry::statusEffect),
			Codec.INT.optionalFieldOf("base_duration", 1200).forGetter(FermentationStatusEffectEntry::baseDuration),
			StatusEffectPotencyEntry.CODEC.listOf().optionalFieldOf("potency", List.of(new StatusEffectPotencyEntry(0, 0, 0))).forGetter(FermentationStatusEffectEntry::potencyEntries)
	).apply(i, FermentationStatusEffectEntry::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, FermentationStatusEffectEntry> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.MOB_EFFECT), FermentationStatusEffectEntry::statusEffect,
			ByteBufCodecs.VAR_INT, FermentationStatusEffectEntry::baseDuration,
			StatusEffectPotencyEntry.PACKET_CODEC.apply(ByteBufCodecs.list()), FermentationStatusEffectEntry::potencyEntries,
			FermentationStatusEffectEntry::new
	);
	
	public record StatusEffectPotencyEntry(float minAlcPercent, float minThickness, int potency) {
		
		public static final Codec<StatusEffectPotencyEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.FLOAT.optionalFieldOf("min_alc", 0.0F).forGetter(StatusEffectPotencyEntry::minAlcPercent),
				Codec.FLOAT.optionalFieldOf("min_thickness", 0.0F).forGetter(StatusEffectPotencyEntry::minThickness),
				Codec.INT.optionalFieldOf("potency", 0).forGetter(StatusEffectPotencyEntry::potency)
		).apply(i, StatusEffectPotencyEntry::new));
		
		public static final StreamCodec<ByteBuf, StatusEffectPotencyEntry> PACKET_CODEC = StreamCodec.composite(
				ByteBufCodecs.FLOAT, StatusEffectPotencyEntry::minAlcPercent,
				ByteBufCodecs.FLOAT, StatusEffectPotencyEntry::minThickness,
				ByteBufCodecs.VAR_INT, StatusEffectPotencyEntry::potency,
				StatusEffectPotencyEntry::new
		);
		
	}
	
}
