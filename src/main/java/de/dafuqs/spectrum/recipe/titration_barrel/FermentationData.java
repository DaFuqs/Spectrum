package de.dafuqs.spectrum.recipe.titration_barrel;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;

import java.util.*;

public record FermentationData(
		float fermentationSpeedMod,
		float angelsSharePercentPerMcDay,
		List<FermentationStatusEffectEntry> statusEffectEntries
) {
	
	public static final FermentationData DEFAULT = new FermentationData(1f, 0.1f, List.of());
	
	public static final Codec<FermentationData> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.FLOAT.optionalFieldOf("fermentation_speed_mod", 1f).forGetter(FermentationData::fermentationSpeedMod),
			Codec.FLOAT.optionalFieldOf("angels_share_percent_per_mc_day", 0.1f).forGetter(FermentationData::angelsSharePercentPerMcDay),
			FermentationStatusEffectEntry.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FermentationData::statusEffectEntries)
	).apply(i, FermentationData::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, FermentationData> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, FermentationData::fermentationSpeedMod,
			ByteBufCodecs.FLOAT, FermentationData::angelsSharePercentPerMcDay,
			FermentationStatusEffectEntry.PACKET_CODEC.apply(ByteBufCodecs.list()), FermentationData::statusEffectEntries,
			FermentationData::new
	);
	
}
