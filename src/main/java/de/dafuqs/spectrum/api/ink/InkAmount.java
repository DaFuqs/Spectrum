package de.dafuqs.spectrum.api.ink;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.color.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;

	public record InkAmount(InkColor color, long amount) {
	
	public static final Codec<InkAmount> CODEC = RecordCodecBuilder.create(i -> i.group(
			InkColor.CODEC.fieldOf("color").forGetter(InkAmount::color),
			Codec.LONG.fieldOf("amount").forGetter(InkAmount::amount)
	).apply(i, InkAmount::new));
	
	public static final StreamCodec<ByteBuf, InkAmount> STREAM_CODEC = StreamCodec.composite(
			InkColor.PACKET_CODEC, InkAmount::color,
			ByteBufCodecs.VAR_LONG, InkAmount::amount,
			InkAmount::new
	);
	
}