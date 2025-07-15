package de.dafuqs.spectrum.recipe.crystallarieum;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.crafting.*;

public record CrystallarieumCatalyst(Ingredient ingredient, float growthAccelerationMod, float inkConsumptionMod, float consumeChancePerSecond) {
	
	public static final CrystallarieumCatalyst EMPTY = new CrystallarieumCatalyst(Ingredient.EMPTY, 0, 0, 0);
	
	public static final Codec<CrystallarieumCatalyst> CODEC = RecordCodecBuilder.create(i -> i.group(
			Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CrystallarieumCatalyst::ingredient),
			Codec.FLOAT.fieldOf("growth_acceleration_mod").forGetter(CrystallarieumCatalyst::growthAccelerationMod),
			Codec.FLOAT.fieldOf("ink_consumption_mod").forGetter(CrystallarieumCatalyst::inkConsumptionMod),
			Codec.FLOAT.fieldOf("consume_chance_per_second").forGetter(CrystallarieumCatalyst::consumeChancePerSecond)
	).apply(i, CrystallarieumCatalyst::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CrystallarieumCatalyst> PACKET_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC, CrystallarieumCatalyst::ingredient,
			ByteBufCodecs.FLOAT, CrystallarieumCatalyst::growthAccelerationMod,
			ByteBufCodecs.FLOAT, CrystallarieumCatalyst::inkConsumptionMod,
			ByteBufCodecs.FLOAT, CrystallarieumCatalyst::consumeChancePerSecond,
			CrystallarieumCatalyst::new
	);
	
}
