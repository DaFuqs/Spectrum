package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;

import java.util.*;

public record WrappedPresentComponent(PresentBlock.WrappingPaper variant, Map<InkColor, Integer> colors) {
	
	public static final WrappedPresentComponent DEFAULT = new WrappedPresentComponent(PresentBlock.WrappingPaper.RED, Map.of());
	
	public static final Codec<WrappedPresentComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			StringRepresentable.fromEnum(PresentBlock.WrappingPaper::values).fieldOf("variant").forGetter(WrappedPresentComponent::variant),
			Codec.unboundedMap(InkColor.CODEC, ExtraCodecs.POSITIVE_INT).fieldOf("colors").forGetter(WrappedPresentComponent::colors)
	).apply(instance, WrappedPresentComponent::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, WrappedPresentComponent> PACKET_CODEC = StreamCodec.composite(
			PacketCodecHelper.enumOf(PresentBlock.WrappingPaper::values), WrappedPresentComponent::variant,
			ByteBufCodecs.map(HashMap::new, InkColor.PACKET_CODEC, ByteBufCodecs.VAR_INT), WrappedPresentComponent::colors,
			WrappedPresentComponent::new
	);
	
}