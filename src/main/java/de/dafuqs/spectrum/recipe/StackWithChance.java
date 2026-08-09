package de.dafuqs.spectrum.recipe;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

public record StackWithChance(ItemStack stack, float chance) {
	
	public static final Codec<StackWithChance> CODEC = RecordCodecBuilder.create(i -> i.group(
			MapCodec.assumeMapUnsafe(ItemStack.CODEC).forGetter(StackWithChance::stack),
			ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("chance", 1.0f).forGetter(StackWithChance::chance)
	).apply(i, StackWithChance::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, StackWithChance> STREAM_CODEC = PacketCodecHelper.tuple(
			ItemStack.STREAM_CODEC, o -> o.stack,
			ByteBufCodecs.FLOAT, o -> o.chance,
			StackWithChance::new
	);
	
}
