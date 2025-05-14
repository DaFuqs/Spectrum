package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;

public record PairedItemComponent(long signature) {
	
	public static final Codec<PairedItemComponent> CODEC = Codec.LONG.xmap(PairedItemComponent::new, PairedItemComponent::signature);
	
	public static final StreamCodec<ByteBuf, PairedItemComponent> PACKET_CODEC = ByteBufCodecs.VAR_LONG.map(PairedItemComponent::new, PairedItemComponent::signature);
	
}
