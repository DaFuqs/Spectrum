package de.dafuqs.spectrum.recipe.crystallarieum;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.crafting.*;

public record CrystallarieumAdditive(Ingredient ingredient, float growthAccelerationMod, float inkConsumptionMod, float consumeChancePerSecond) {
	
	public static final CrystallarieumAdditive EMPTY = new CrystallarieumAdditive(Ingredient.EMPTY, 0, 0, 0);
	
	public static final Codec<CrystallarieumAdditive> CODEC = RecordCodecBuilder.create(i -> i.group(
			Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CrystallarieumAdditive::ingredient),
			Codec.FLOAT.fieldOf("growth_acceleration_mod").forGetter(CrystallarieumAdditive::growthAccelerationMod),
			Codec.FLOAT.fieldOf("ink_consumption_mod").forGetter(CrystallarieumAdditive::inkConsumptionMod),
			Codec.FLOAT.fieldOf("consume_chance_per_second").forGetter(CrystallarieumAdditive::consumeChancePerSecond)
	).apply(i, CrystallarieumAdditive::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CrystallarieumAdditive> PACKET_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC, CrystallarieumAdditive::ingredient,
			ByteBufCodecs.FLOAT, CrystallarieumAdditive::growthAccelerationMod,
			ByteBufCodecs.FLOAT, CrystallarieumAdditive::inkConsumptionMod,
			ByteBufCodecs.FLOAT, CrystallarieumAdditive::consumeChancePerSecond,
			CrystallarieumAdditive::new
	);
	
}
