package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.level.block.*;

public record InertiaComponent(Block lastMined, long count) {
	
	public static final InertiaComponent DEFAULT = new InertiaComponent(Blocks.AIR, 0);
	
	public static final Codec<InertiaComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("last_mined").forGetter(InertiaComponent::lastMined),
			Codec.LONG.fieldOf("count").forGetter(InertiaComponent::count)
	).apply(i, InertiaComponent::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, InertiaComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.BLOCK),
			InertiaComponent::lastMined,
			ByteBufCodecs.VAR_LONG,
			InertiaComponent::count,
			InertiaComponent::new
	);
	
}
