package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;

import java.util.*;

public record WrappedPresentComponent(boolean wrapped, PresentBlock.WrappingPaper variant, Map<Integer, Integer> colors) {
	
	public static final WrappedPresentComponent DEFAULT = new WrappedPresentComponent(false, PresentBlock.WrappingPaper.RED, Map.of());
	
	public static final Codec<WrappedPresentComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.optionalFieldOf("wrapped", false).forGetter(WrappedPresentComponent::wrapped),
			StringRepresentable.fromEnum(PresentBlock.WrappingPaper::values).fieldOf("variant").forGetter(WrappedPresentComponent::variant),
			Codec.unboundedMap(Codec.INT, ExtraCodecs.POSITIVE_INT).fieldOf("colors").forGetter(WrappedPresentComponent::colors)
	).apply(instance, WrappedPresentComponent::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, WrappedPresentComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, WrappedPresentComponent::wrapped,
			PacketCodecHelper.enumOf(PresentBlock.WrappingPaper::values), WrappedPresentComponent::variant,
			ByteBufCodecs.map(HashMap::new, ByteBufCodecs.VAR_INT, ByteBufCodecs.VAR_INT), WrappedPresentComponent::colors,
			WrappedPresentComponent::new
	);
	
}
