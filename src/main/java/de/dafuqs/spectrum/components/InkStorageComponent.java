package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;

import java.util.*;

public record InkStorageComponent(long maxEnergyTotal, long maxPerColor, Map<InkColor, Long> storedEnergy) {
	
	public static final Codec<InkStorageComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.LONG.fieldOf("max_energy_total").forGetter(c -> c.maxEnergyTotal),
			Codec.LONG.fieldOf("max_per_color").forGetter(c -> c.maxPerColor),
			ExtraCodecs.strictUnboundedMap(InkColor.CODEC, Codec.LONG).fieldOf("stored_energy").forGetter(c -> c.storedEnergy)
	).apply(instance, InkStorageComponent::new));
	
	public static final StreamCodec<ByteBuf, InkStorageComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG, c -> c.maxEnergyTotal,
			ByteBufCodecs.VAR_LONG, c -> c.maxPerColor,
			ByteBufCodecs.map(HashMap::new, InkColor.PACKET_CODEC, ByteBufCodecs.VAR_LONG), c -> c.storedEnergy,
			InkStorageComponent::new
	);
	
	public InkStorageComponent(InkStorage storage) {
		this(storage.getMaxTotal(), storage.getMaxPerColor(), storage.getEnergy());
	}
	
}
