package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.particle.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record InkPastelPayload(List<InkAmount> inkAmount) implements PastelPayload {
	
	public static final MapCodec<InkPastelPayload> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			InkAmount.CODEC.listOf().fieldOf("ink").forGetter(InkPastelPayload::inkAmount)
	).apply(i, InkPastelPayload::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, InkPastelPayload> STREAM_CODEC = StreamCodec.composite(
			InkAmount.STREAM_CODEC.apply(ByteBufCodecs.list()), InkPastelPayload::inkAmount,
			InkPastelPayload::new
	);
	
	public MapCodec<InkPastelPayload> codec() {
		return CODEC;
	}
	
	public StreamCodec<RegistryFriendlyByteBuf, InkPastelPayload> streamCodec() {
		return STREAM_CODEC;
	}
	
}
