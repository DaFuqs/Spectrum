package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;

public record ShootingStarComponent(int remainingHits, boolean hardened) {
	
	public static final ShootingStarComponent DEFAULT = new ShootingStarComponent(5, false);
	
	public static final Codec<ShootingStarComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("remaining_hits").forGetter(c -> c.remainingHits),
			Codec.BOOL.fieldOf("hardened").forGetter(c -> c.hardened)
	).apply(i, ShootingStarComponent::new));
	
	public static final StreamCodec<ByteBuf, ShootingStarComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, c -> c.remainingHits,
			ByteBufCodecs.BOOL, c -> c.hardened,
			ShootingStarComponent::new
	);
	
}
